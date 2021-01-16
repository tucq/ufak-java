package com.ufak.usr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.product.entity.ProductInfo;
import com.ufak.product.entity.ProductPrice;
import com.ufak.product.entity.ProductSpecs;
import com.ufak.product.service.IProductInfoService;
import com.ufak.product.service.IProductPriceService;
import com.ufak.product.service.IProductSpecsService;
import com.ufak.usr.entity.ShoppingCar;
import com.ufak.usr.service.IShoppingCarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date:   2020-03-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="购物车")
@RestController
@RequestMapping("/shopping/car")
public class ShoppingCarController extends JeecgController<ShoppingCar, IShoppingCarService> {
	@Autowired
	private IShoppingCarService shoppingCarService;
	@Autowired
	private IProductInfoService productInfoService;
	@Autowired
	private IProductSpecsService productSpecsService;
	@Autowired
	private IProductPriceService productPriceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="购物车-分页列表查询", notes="购物车-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String userId = req.getParameter("userId");
		String isCheck = req.getParameter("isCheck");
		Map paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(userId)){
			paramMap.put("userId",userId);
		}
		if(StringUtils.isNotBlank(isCheck)){
			paramMap.put("isCheck",isCheck);
		}
		IPage<ShoppingCar> pageList = shoppingCarService.selectPage(pageNo,pageSize,paramMap);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param shoppingCar
	 * @return
	 */
	@ApiOperation(value="购物车-添加", notes="购物车-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ShoppingCar shoppingCar) {
		QueryWrapper qw = new QueryWrapper();
		qw.eq("user_id",shoppingCar.getUserId());
		qw.eq("product_id",shoppingCar.getProductId());
		qw.eq("specs1_id",shoppingCar.getSpecs1Id());
		if(StringUtils.isNotEmpty(shoppingCar.getSpecs2Id())){
			qw.eq("specs2_id",shoppingCar.getSpecs2Id());
		}
		ShoppingCar car = shoppingCarService.getOne(qw);
		if(car == null){
			shoppingCarService.save(shoppingCar);
		}else{
			car.setBuyNum(car.getBuyNum() + shoppingCar.getBuyNum());
			shoppingCarService.updateById(car);
		}

		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param shoppingCar
	 * @return
	 */
	@AutoLog(value = "购物车-编辑")
	@ApiOperation(value="购物车-编辑", notes="购物车-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ShoppingCar shoppingCar) {
		shoppingCarService.updateById(shoppingCar);
		return Result.ok("编辑成功!");
	}


	/**
	 * 立即购买查商品信息
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/buyNow")
	public Result<?> buyNow(HttpServletRequest req) {
		String productId = req.getParameter("productId");
		String specs1Id = req.getParameter("specs1Id");
		String specs2Id = req.getParameter("specs2Id");
		String buyNum = req.getParameter("buyNum");
		ShoppingCar shoppingCar= new ShoppingCar();

		ProductInfo product = productInfoService.getById(productId);
		ProductSpecs specs1 = productSpecsService.getById(specs1Id);
		QueryWrapper<ProductPrice> qryPrice = new QueryWrapper<>();
		qryPrice.eq("product_id",productId);
		qryPrice.eq("specs1_id",specs1Id);
		if(StringUtils.isNotBlank(specs2Id) && !"null".equals(specs2Id)){
			qryPrice.eq("specs2_id",specs2Id);
		}
		ProductPrice price = productPriceService.getOne(qryPrice);
		shoppingCar.setProductName(product.getName());
		shoppingCar.setViewImage(product.getImage().split(",")[0]);
		String specsName = specs1.getSpecsTitle();
		if(StringUtils.isNotBlank(specs2Id) && !"null".equals(specs2Id)){
			ProductSpecs specs2 = productSpecsService.getById(specs2Id);
			specsName = " " + specs2.getSpecsTitle();
		}
		shoppingCar.setSpecsName(specsName);
		shoppingCar.setPrice(price.getPrice().toString());
		shoppingCar.setBuyNum(Integer.valueOf(buyNum));
		shoppingCar.setFreightAmount(product.getFreightAmount());
		return Result.ok(shoppingCar);
	}


	/**
	 * 通过id删除
	 *
	 * @return
	 */
	@AutoLog(value = "购物车-通过id删除")
	@ApiOperation(value="购物车-通过id删除", notes="购物车-通过id删除")
	@PostMapping(value = "/delete")
	public Result<?> delete(@RequestBody ShoppingCar shoppingCar) {
		shoppingCarService.removeById(shoppingCar.getId());
		return Result.ok("删除成功!");
	}

	/**
	 * 更新选中状态
	 * @param shoppingCar
	 * @return
	 */
	@PostMapping(value = "/checked")
	public Result<?> checked(@RequestBody ShoppingCar shoppingCar) {
		shoppingCarService.updateById(shoppingCar);
		return Result.ok("更新成功!");
	}

	@PostMapping(value = "/updateBuyNum")
	public Result<?> updateBuyNum(@RequestBody ShoppingCar shoppingCar) {
		shoppingCarService.updateById(shoppingCar);
		return Result.ok("更新成功!");
	}

	/**
	 * 更新选中状态
	 * @param shoppingCar
	 * @return
	 */
	@PostMapping(value = "/allChecked")
	public Result<?> allChecked(@RequestBody ShoppingCar shoppingCar) {
		UpdateWrapper uw = new UpdateWrapper();
		uw.eq("user_id",shoppingCar.getUserId());
		uw.set("is_check",shoppingCar.getIsCheck());
		shoppingCarService.update(uw);
		return Result.ok("更新成功!");
	}


	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "购物车-批量删除")
	@ApiOperation(value="购物车-批量删除", notes="购物车-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.shoppingCarService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "购物车-通过id查询")
	@ApiOperation(value="购物车-通过id查询", notes="购物车-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ShoppingCar shoppingCar = shoppingCarService.getById(id);
		return Result.ok(shoppingCar);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param shoppingCar
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ShoppingCar shoppingCar) {
      return super.exportXls(request, shoppingCar, ShoppingCar.class, "购物车");
  }

  /**
   * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      return super.importExcel(request, response, ShoppingCar.class);
  }

}
