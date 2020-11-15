package com.ufak.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.order.entity.OrderDetail;
import com.ufak.order.service.IOrderDetailService;
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
 * @Description: 订单明细
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="订单明细")
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController extends JeecgController<OrderDetail, IOrderDetailService> {
	@Autowired
	private IOrderDetailService orderDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param orderDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "订单明细-分页列表查询")
	@ApiOperation(value="订单明细-分页列表查询", notes="订单明细-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(OrderDetail orderDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<OrderDetail> queryWrapper = QueryGenerator.initQueryWrapper(orderDetail, req.getParameterMap());
		Page<OrderDetail> page = new Page<OrderDetail>(pageNo, pageSize);
		IPage<OrderDetail> pageList = orderDetailService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param orderDetail
	 * @return
	 */
	@AutoLog(value = "订单明细-添加")
	@ApiOperation(value="订单明细-添加", notes="订单明细-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody OrderDetail orderDetail) {
		orderDetailService.save(orderDetail);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param orderDetail
	 * @return
	 */
	@AutoLog(value = "订单明细-编辑")
	@ApiOperation(value="订单明细-编辑", notes="订单明细-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody OrderDetail orderDetail) {
		orderDetailService.updateById(orderDetail);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单明细-通过id删除")
	@ApiOperation(value="订单明细-通过id删除", notes="订单明细-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		orderDetailService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "订单明细-批量删除")
	@ApiOperation(value="订单明细-批量删除", notes="订单明细-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.orderDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单明细-通过id查询")
	@ApiOperation(value="订单明细-通过id查询", notes="订单明细-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		OrderDetail orderDetail = orderDetailService.getById(id);
		return Result.ok(orderDetail);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param orderDetail
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, OrderDetail orderDetail) {
      return super.exportXls(request, orderDetail, OrderDetail.class, "订单明细");
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
      return super.importExcel(request, response, OrderDetail.class);
  }

}
