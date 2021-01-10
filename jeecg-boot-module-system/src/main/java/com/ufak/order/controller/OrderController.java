package com.ufak.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.common.Constants;
import com.ufak.order.entity.Order;
import com.ufak.order.entity.OrderDetail;
import com.ufak.order.service.IOrderDetailService;
import com.ufak.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="订单主表")
@RestController
@RequestMapping("/order")
public class OrderController extends JeecgController<Order, IOrderService> {
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderDetailService orderDetailService;

	/**
	 * 分页列表查询
	 *
	 * @param order
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "订单主表-分页列表查询")
	@ApiOperation(value="订单主表-分页列表查询", notes="订单主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Order order,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String orderNo = req.getParameter("orderNo");
		String orderStatus = req.getParameter("orderStatus");
		String telephone = req.getParameter("telephone");
		Map paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(orderNo)){
			paramMap.put("orderNo",orderNo);
		}
		if(StringUtils.isNotBlank(orderStatus)){
			paramMap.put("orderStatus",orderStatus);
		}
		if(StringUtils.isNotBlank(telephone)){
			paramMap.put("telephone",telephone);
		}
		IPage<Order> pageList = orderService.queryPageList(pageNo,pageSize,paramMap);
		return Result.ok(pageList);
	}

	/**
	 * 移动端订单列表
	 * @param order
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/app/list")
	public Result<?> queryAppPageList(Order order,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String userId = req.getParameter("userId");
		String orderStatus = req.getParameter("orderStatus");
		Map paramMap = new HashMap<>();
		paramMap.put("userId",userId);
		if(StringUtils.isNotBlank(orderStatus) && !"null".equals(orderStatus)){
			paramMap.put("orderStatus",orderStatus.split(","));
		}
		IPage<Order> pageList = orderService.queryAppPageList(pageNo,pageSize,paramMap);
		return Result.ok(pageList);
	}

	/**
	 * 获取订单详情
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	public Result<?> getByOrderId(@PathVariable String orderId) {
		Order order = orderService.getById(orderId);
		QueryWrapper<OrderDetail> qw = new QueryWrapper<>();
		qw.eq("order_id", orderId);
		List<OrderDetail> orderDetails = orderDetailService.queryByOrderId(orderId);
		order.setOrderDetails(orderDetails);
		return Result.ok(order);
	}

	/**
	 * 订单取消
	 * @param orderId
	 * @return
	 */
	@PostMapping(value = "/cancel")
	public Result<?> cancel(@RequestParam(name="orderId",required=true) String orderId) {
		try {
			Order order = orderService.getById(orderId);
			if(order == null){
				return Result.error("该订单号不存在");
			}
			if(!Constants.WAIT_PAY.equals(order.getOrderStatus())){
				return Result.error("非待支付状态订单无法取消");
			}
			orderService.cancelOrder(orderId);
			return Result.ok("订单取消成功");
		}catch (JeecgBootException e){
			return Result.error(e.getMessage());// 乐观锁检测库存已被更改
		}catch (Exception e) {
			log.error("订单取消:{}",e);
			return Result.error("订单取消异常，请联系客服！");
		}
	}


	/**
	 * 再次购买
	 * @param orderId
	 * @return
	 */
	@PostMapping(value = "/buy/again")
	public Result<?> buyAgain(@RequestParam(name="orderId") String orderId) {
		try {
			orderService.buyAgain(orderId);
			return Result.ok("操作成功");
		}catch (JeecgBootException e){
			return Result.error(e.getMessage());
		}catch (Exception e) {
			log.error("再次购买异常:{}",e);
			return Result.error("再次购买异常，请联系客服！");
		}
	}


	/**
	 * 确认收货
	 * @param orderId
	 * @return
	 */
	@PostMapping(value = "/confirm/receive")
	public Result<?> confirmReceive(@RequestParam(name="orderId") String orderId) {
		Order order = new Order();
		order.setId(orderId);
		order.setOrderStatus(Constants.COMPLETED);
		orderService.updateById(order);
		return Result.ok();
	}


	/**
	 * 申请售后
	 * @param orderDetail
	 * @return
	 */
	@PostMapping(value = "/apply/afterSale")
	public Result<?> afterSale(@RequestBody OrderDetail orderDetail) {
		orderDetailService.save(orderDetail);
		return Result.ok("添加成功！");
	}

	/**
	 * 发货
	 * @param order
	 * @return
	 */
	@PostMapping(value = "/send/goods")
	public Result<?> sendGoods(@RequestBody Order order) {
		LambdaUpdateWrapper<Order> uw = new LambdaUpdateWrapper();
		uw.set(Order::getOrderStatus,Constants.WAIT_RECEIVE);
		uw.set(Order::getLogisticsNo,order.getLogisticsNo());
		uw.set(Order::getUpdateTime,new Date());
		uw.eq(Order::getId,order.getId());
		orderService.update(uw);
		return Result.ok("发货成功！");
	}


	/**
	 * 申请开票
	 * @param orderDetail
	 * @return
	 */
	@PostMapping(value = "/open/invoice")
	public Result<?> add(@RequestBody OrderDetail orderDetail) {
		//todo
		orderDetailService.save(orderDetail);
		return Result.ok("添加成功！");
	}

	/**
	 * 添加
	 *
	 * @param order
	 * @return
	 */
	@AutoLog(value = "订单主表-添加")
	@ApiOperation(value="订单主表-添加", notes="订单主表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Order order) {
		orderService.save(order);
		return Result.ok("添加成功！");
	}


	/**
	 * 编辑
	 *
	 * @param order
	 * @return
	 */
	@AutoLog(value = "订单主表-编辑")
	@ApiOperation(value="订单主表-编辑", notes="订单主表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Order order) {
		orderService.updateById(order);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单主表-通过id删除")
	@ApiOperation(value="订单主表-通过id删除", notes="订单主表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			orderService.deleteOrder(id);
			return Result.ok("删除成功!");
		} catch (Exception e) {
			log.error("订单删除异常：{}",e);
			return Result.error("订单删除异常,请联系客服!");
		}
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "订单主表-批量删除")
	@ApiOperation(value="订单主表-批量删除", notes="订单主表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.orderService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单主表-通过id查询")
	@ApiOperation(value="订单主表-通过id查询", notes="订单主表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Order order = orderService.getById(id);
		return Result.ok(order);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param order
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, Order order) {
      return super.exportXls(request, order, Order.class, "订单主表");
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
      return super.importExcel(request, response, Order.class);
  }

}
