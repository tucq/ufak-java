package com.ufak.aftesale.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.entity.AfterSaleRefund;
import com.ufak.aftesale.service.IAfterSaleRefundService;
import com.ufak.aftesale.service.IAfterSaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
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
	@AutoLog(value = "退款明细-分页列表查询")
	@ApiOperation(value="退款明细-分页列表查询", notes="退款明细-分页列表查询")
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
	@AutoLog(value = "退款明细-添加")
	@ApiOperation(value="退款明细-添加", notes="退款明细-添加")
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
	@AutoLog(value = "退款明细-编辑")
	@ApiOperation(value="退款明细-编辑", notes="退款明细-编辑")
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
	@AutoLog(value = "退款明细-通过id删除")
	@ApiOperation(value="退款明细-通过id删除", notes="退款明细-通过id删除")
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
	@AutoLog(value = "退款明细-批量删除")
	@ApiOperation(value="退款明细-批量删除", notes="退款明细-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.afterSaleRefundService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "退款明细-通过id查询")
	@ApiOperation(value="退款明细-通过id查询", notes="退款明细-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AfterSaleRefund afterSaleRefund = afterSaleRefundService.getById(id);
		return Result.ok(afterSaleRefund);
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
