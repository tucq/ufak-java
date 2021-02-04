package com.ufak.product.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.common.Constants;
import com.ufak.product.entity.ProductPrice;
import com.ufak.product.service.IProductPriceService;
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
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品价格库存
 * @Author: jeecg-boot
 * @Date:   2020-03-08
 * @Version: V1.0
 */
@Slf4j
@Api(tags="商品价格库存")
@RestController
@RequestMapping("/product/price")
public class ProductPriceController extends JeecgController<ProductPrice, IProductPriceService> {
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
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String productId = req.getParameter("productId");
		Map paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(productId)){
			paramMap.put("productId",productId);
		}
		IPage<ProductPrice> pageList = productPriceService.selectPage(pageNo,pageSize,paramMap);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param productPrice
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ProductPrice productPrice) {
		productPriceService.save(productPrice);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param productPrice
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ProductPrice productPrice) {
		productPriceService.updateById(productPrice);
		return Result.ok("编辑成功!");
	}

	/**
	 * 编辑价格
	 * @return
	 */
	@PutMapping(value = "/update/price")
	public Result<?> updatePrice(@RequestBody JSONObject jsonObject) {
		JSONArray array = jsonObject.getJSONArray("productPriceList");
		List<ProductPrice> productPriceList = array.toJavaList(ProductPrice.class);
		for(ProductPrice price : productPriceList){
			productPriceService.updateById(price);
		}

		JSONObject json = jsonObject.getJSONObject("defaultFlag");
		String productId = json.getString("productId");
		String id = json.getString("id");
		if(StringUtils.isNotEmpty(id)){
			UpdateWrapper uw = new UpdateWrapper();
			uw.set("default_flag", Constants.NO);
			uw.eq("product_id",productId);
			productPriceService.update(uw);
			uw.set("default_flag", Constants.YES);
			uw.eq("id",id);
			productPriceService.update(uw);
		}
		return Result.ok("价格库存设置成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		productPriceService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.productPriceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ProductPrice productPrice = productPriceService.getById(id);
		return Result.ok(productPrice);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param productPrice
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ProductPrice productPrice) {
      return super.exportXls(request, productPrice, ProductPrice.class, "商品价格库存");
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
      return super.importExcel(request, response, ProductPrice.class);
  }

}
