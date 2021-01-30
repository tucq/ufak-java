package com.ufak.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.order.entity.OrderUnpaid;
import com.ufak.order.service.IOrderService;
import com.ufak.order.service.IOrderUnpaidService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 未付款订单签名
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="未付款订单签名")
@RestController
@RequestMapping("/orderUnpaid")
public class OrderUnpaidController extends JeecgController<OrderUnpaid, IOrderUnpaidService> {
	@Autowired
	private IOrderUnpaidService orderUnpaidService;
	@Autowired
	private IOrderService orderService;
	
	/**
	 * 分页列表查询
	 *
	 * @param orderUnpaid
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "未付款订单签名-分页列表查询")
	@ApiOperation(value="未付款订单签名-分页列表查询", notes="未付款订单签名-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(OrderUnpaid orderUnpaid,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OrderUnpaid> queryWrapper = QueryGenerator.initQueryWrapper(orderUnpaid, req.getParameterMap());
		Page<OrderUnpaid> page = new Page<OrderUnpaid>(pageNo, pageSize);
		IPage<OrderUnpaid> pageList = orderUnpaidService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param orderUnpaid
	 * @return
	 */
	@AutoLog(value = "未付款订单签名-添加")
	@ApiOperation(value="未付款订单签名-添加", notes="未付款订单签名-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OrderUnpaid orderUnpaid) {
		orderUnpaidService.save(orderUnpaid);
		return Result.ok("添加成功！");
	}

	 /**
	  * 根据订单id查找未付款签名
	  * @param orderId
	  * @return
	  */
	 @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	 public Result<?> getByOrderId(@PathVariable String orderId) {
//		 Order order = orderService.getById(orderId);
//		 long diff = new Date().getTime() - order.getCreateTime().getTime();
//		 long d = 30 * 60 * 1000;
//		 if(diff > d){
//		 	order.setOrderStatus(Constants.CANCELLED);
//		 	orderService.updateById(order);
//		 	return Result.error("非常抱歉,该笔订单已退款");
//		 }
		 QueryWrapper<OrderUnpaid> qw = new QueryWrapper<>();
		 qw.eq("order_id", orderId);
		 OrderUnpaid orderUnpaid = orderUnpaidService.getOne(qw);
		 return Result.ok(orderUnpaid);
	 }

	/**
	 * 编辑
	 *
	 * @param orderUnpaid
	 * @return
	 */
	@AutoLog(value = "未付款订单签名-编辑")
	@ApiOperation(value="未付款订单签名-编辑", notes="未付款订单签名-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OrderUnpaid orderUnpaid) {
		orderUnpaidService.updateById(orderUnpaid);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "未付款订单签名-通过id删除")
	@ApiOperation(value="未付款订单签名-通过id删除", notes="未付款订单签名-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		orderUnpaidService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "未付款订单签名-批量删除")
	@ApiOperation(value="未付款订单签名-批量删除", notes="未付款订单签名-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.orderUnpaidService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "未付款订单签名-通过id查询")
	@ApiOperation(value="未付款订单签名-通过id查询", notes="未付款订单签名-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OrderUnpaid orderUnpaid = orderUnpaidService.getById(id);
		return Result.ok(orderUnpaid);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param orderUnpaid
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, OrderUnpaid orderUnpaid) {
      return super.exportXls(request, orderUnpaid, OrderUnpaid.class, "未付款订单签名");
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
      return super.importExcel(request, response, OrderUnpaid.class);
  }

}
