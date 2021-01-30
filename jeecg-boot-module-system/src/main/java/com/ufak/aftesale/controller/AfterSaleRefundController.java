package com.ufak.aftesale.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.entity.AfterSaleRefund;
import com.ufak.aftesale.service.IAfterSaleRefundService;
import com.ufak.aftesale.service.IAfterSaleService;
import com.ufak.common.Constants;
import com.ufak.order.entity.Order;
import com.ufak.order.entity.OrderDetail;
import com.ufak.order.service.IOrderDetailService;
import com.ufak.order.service.IOrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
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
 * @Description: 退款明细
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="退款明细")
@RestController
@RequestMapping("/afterSaleRefund")
public class AfterSaleRefundController extends JeecgController<AfterSaleRefund, IAfterSaleRefundService> {
	@Autowired
	private IAfterSaleRefundService afterSaleRefundService;
	@Autowired
	private IAfterSaleService afterSaleService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderDetailService orderDetailService;

	/**
	 * 申请退款
	 * @param jsonObject
	 * @return
	 */
	 @PostMapping(value = "/apply")
	 public Result<?> apply(@RequestBody JSONObject jsonObject) {
	 	 String orderId = jsonObject.getString("orderId");
		 AfterSaleRefund refund = new AfterSaleRefund();
		 refund.setRefundReason(jsonObject.getString("refundReason"));
		 refund.setRemark(jsonObject.getString("remark"));
		 refund.setRefundContact(jsonObject.getString("refundContact"));
		 refund.setRefundTelephone(jsonObject.getString("refundTelephone"));
		 try {
			 afterSaleRefundService.apply(orderId,refund);
			 return Result.ok("申请退款成功");
		 } catch (JeecgBootException e){
			 log.error("申请退款异常：{}",e);
			 return Result.error(e.getMessage());
		 } catch (Exception e) {
			 log.error("申请退款异常：{}",e);
			 return Result.error("申请退款异常,请联系客服");
		 }
	 }

	/**
	 * 申请退款取消
	 * @param afterSaleId
	 * @return
	 */
	@PostMapping(value = "/cancel")
	public Result<?> cancel(@RequestParam(name="afterSaleId",required=true) String afterSaleId) {
		try {
			afterSaleRefundService.cancel(afterSaleId);
			return Result.ok("取消退款成功");
		} catch (JeecgBootException e){
			log.error("取消退款异常",e);
			return Result.error(e.getMessage());
		} catch (Exception e) {
			log.error("取消退款异常",e);
			return Result.error("取消退款异常,请联系客服");
		}
	}


	/**
	 * 查看详情
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/view")
	public Result<?> view(@RequestParam(name="id",required=true) String id) {
		Map<String,Object> resultMap = new HashMap<>();
		AfterSale afterSale = afterSaleService.getById(id);
		QueryWrapper<AfterSaleRefund> qw = new QueryWrapper<>();
		qw.eq("after_sale_id",id);
		AfterSaleRefund afterSaleRefund = afterSaleRefundService.getOne(qw);
		resultMap.put("afterSale",afterSale);
		resultMap.put("afterSaleRefund",afterSaleRefund);
		return Result.ok(resultMap);
	}



	/**
	 * 分页列表查询
	 *
	 * @param afterSaleRefund
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AfterSaleRefund afterSaleRefund,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AfterSaleRefund> queryWrapper = QueryGenerator.initQueryWrapper(afterSaleRefund, req.getParameterMap());
		Page<AfterSaleRefund> page = new Page<AfterSaleRefund>(pageNo, pageSize);
		IPage<AfterSaleRefund> pageList = afterSaleRefundService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param afterSaleRefund
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AfterSaleRefund afterSaleRefund) {
		afterSaleRefundService.save(afterSaleRefund);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param afterSaleRefund
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AfterSaleRefund afterSaleRefund) {
		afterSaleRefundService.updateById(afterSaleRefund);
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
		afterSaleRefundService.removeById(id);
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
		this.afterSaleRefundService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 * @param afterSaleId
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="afterSaleId",required=true) String afterSaleId) {
		AfterSale afterSale = afterSaleService.getById(afterSaleId);
		LambdaQueryWrapper<AfterSaleRefund> qw = new LambdaQueryWrapper<>();
		qw.eq(AfterSaleRefund::getAfterSaleId,afterSaleId);
		AfterSaleRefund afterSaleRefund = afterSaleRefundService.getOne(qw);
		afterSaleRefund.setAfterSaleNo(afterSale.getAfterSaleNo());
		afterSaleRefund.setRefundFee(afterSale.getRefundFee());
		if(Constants.STATUS_PROCESS.equals(afterSale.getStatus())){
			afterSaleRefund.setStatusText("待处理");
		}else if(Constants.STATUS_COMPLETE.equals(afterSale.getStatus())){
			afterSaleRefund.setStatusText("已完成");
		}else if(Constants.STATUS_CANCEL.equals(afterSale.getStatus())){
			afterSaleRefund.setStatusText("客户取消");
		}

		Order order = orderService.getById(afterSale.getOrderId());
		List<OrderDetail> orderDetails = orderDetailService.queryByOrderId(afterSale.getOrderId());
		Map<String,Object> result = new HashedMap();
		result.put("afterSaleRefund",afterSaleRefund);
		result.put("order",order);
		result.put("orderDetails",orderDetails);
		return Result.ok(result);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param afterSaleRefund
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, AfterSaleRefund afterSaleRefund) {
		return super.exportXls(request, afterSaleRefund, AfterSaleRefund.class, "退款明细");
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
		return super.importExcel(request, response, AfterSaleRefund.class);
	}


}
