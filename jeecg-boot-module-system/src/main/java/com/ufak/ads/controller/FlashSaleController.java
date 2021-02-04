package com.ufak.ads.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.ads.entity.FlashSale;
import com.ufak.ads.service.IFlashSaleService;
import com.ufak.common.Constants;
import com.ufak.order.entity.Order;
import com.ufak.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
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
 * @Description: 限制抢购
 * @Author: jeecg-boot
 * @Date:   2021-01-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags="限制抢购")
@RestController
@RequestMapping("/flashSale")
public class FlashSaleController extends JeecgController<FlashSale, IFlashSaleService> {
	@Autowired
	private IFlashSaleService flashSaleService;
	@Autowired
	private IOrderService orderService;

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
		String state = req.getParameter("state");
		Map paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(state)){
			paramMap.put("state",state);
		}
		IPage<FlashSale> pageList = flashSaleService.selectPage(pageNo,pageSize,paramMap);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param flashSale
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody FlashSale flashSale) {
		flashSaleService.save(flashSale);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param flashSale
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody FlashSale flashSale) {
		flashSaleService.updateById(flashSale);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		flashSaleService.removeById(id);
		return Result.ok("删除成功!");
	}

	 /**
	  * 启用
	  * @param id
	  * @return
	  */
	@PostMapping(value = "/start")
	public Result<?> start(@RequestParam(name="id",required=true) String id) {
		LambdaUpdateWrapper<FlashSale> updateWrapper = new LambdaUpdateWrapper();
		updateWrapper.set(FlashSale::getState, Constants.YES);
		updateWrapper.eq(FlashSale::getId,id);
		flashSaleService.update(updateWrapper);
		return Result.ok("启用成功!");
	}

	 /**
	  * 停用
	  * @param id
	  * @return
	  */
	@PostMapping(value = "/stop")
	public Result<?> stop(@RequestParam(name="id",required=true) String id) {
		LambdaUpdateWrapper<FlashSale> updateWrapper = new LambdaUpdateWrapper();
		updateWrapper.set(FlashSale::getState, Constants.NO);
		updateWrapper.eq(FlashSale::getId,id);
		flashSaleService.update(updateWrapper);
		return Result.ok("停用成功!");
	}

	/**
	 * 秒杀支付取消
	 * @return
	 */
	@PostMapping(value = "/cancel")
	public Result<?> cancel(@RequestBody JSONObject jsonObject) {
		FlashSale flashSale = flashSaleService.getById(jsonObject.getString("flashSaleId"));
		flashSale.setStock(flashSale.getStock() + 1);
		flashSaleService.updateById(flashSale);

		Order order = orderService.getById(jsonObject.getString("orderId"));
		order.setOrderStatus(Constants.CANCELLED);
		orderService.updateById(order);
		return Result.ok("秒杀取消成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.flashSaleService.removeByIds(Arrays.asList(ids.split(",")));
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
		FlashSale flashSale = flashSaleService.getById(id);
		return Result.ok(flashSale);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param flashSale
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, FlashSale flashSale) {
      return super.exportXls(request, flashSale, FlashSale.class, "限制抢购");
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
      return super.importExcel(request, response, FlashSale.class);
  }

}
