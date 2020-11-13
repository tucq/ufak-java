package com.ufak.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.order.entity.Order;
import com.ufak.order.service.IOrderService;
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
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="订单主表")
@RestController
@RequestMapping("/com.ufak/order")
public class OrderController extends JeecgController<Order, IOrderService> {
	@Autowired
	private IOrderService orderService;
	
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
		QueryWrapper<Order> queryWrapper = QueryGenerator.initQueryWrapper(order, req.getParameterMap());
		Page<Order> page = new Page<Order>(pageNo, pageSize);
		IPage<Order> pageList = orderService.page(page, queryWrapper);
		return Result.ok(pageList);
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
		orderService.removeById(id);
		return Result.ok("删除成功!");
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
