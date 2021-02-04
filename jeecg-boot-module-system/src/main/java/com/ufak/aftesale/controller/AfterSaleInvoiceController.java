package com.ufak.aftesale.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.entity.AfterSaleInvoice;
import com.ufak.aftesale.service.IAfterSaleInvoiceService;
import com.ufak.aftesale.service.IAfterSaleService;
import com.ufak.common.Constants;
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
 * @Description: 售后开票明细
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="售后开票明细")
@RestController
@RequestMapping("/afterSaleInvoice")
public class AfterSaleInvoiceController extends JeecgController<AfterSaleInvoice, IAfterSaleInvoiceService> {
	@Autowired
	private IAfterSaleService afterSaleService;
	@Autowired
	private IAfterSaleInvoiceService afterSaleInvoiceService;


	/**
	 * 校验是否已开过票
	 *
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/check")
	public Result<?> checkExpire(@RequestParam String orderId) {
		QueryWrapper<AfterSale> qw = new QueryWrapper();
		qw.eq("order_id",orderId);
		qw.eq("service_type",Constants.AFTER_SALE_INVOICE);
		qw.ne("status", Constants.STATUS_CANCEL);
		int i = afterSaleService.count(qw);
		if(i > 0){
			return Result.error("该笔订单已申请过开票");
		}
		return Result.ok();
	}


	/**
	 * 开票申请
	 * @param afterSaleInvoice
	 * @return
	 */
	@PostMapping(value = "/apply")
	public Result<?> apply(@RequestBody AfterSaleInvoice afterSaleInvoice) {
		try {
			afterSaleInvoiceService.apply(afterSaleInvoice);
			return Result.ok("开票申请成功");
		} catch (JeecgBootException e) {
			log.error("开票申请异常：{}", e);
			return Result.error(e.getMessage());
		} catch (Exception e) {
			log.error("开票申请异常：{}", e);
			return Result.error("开票申请异常,请联系客服");
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
		QueryWrapper<AfterSaleInvoice> qw = new QueryWrapper<>();
		qw.eq("after_sale_id",id);
		AfterSaleInvoice afterSaleInvoice = afterSaleInvoiceService.getOne(qw);
		resultMap.put("afterSale",afterSale);
		resultMap.put("afterSaleInvoice",afterSaleInvoice);

		return Result.ok(resultMap);
	}

	/**
	 * 分页列表查询
	 *
	 * @param afterSaleInvoice
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AfterSaleInvoice afterSaleInvoice,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AfterSaleInvoice> queryWrapper = QueryGenerator.initQueryWrapper(afterSaleInvoice, req.getParameterMap());
		Page<AfterSaleInvoice> page = new Page<AfterSaleInvoice>(pageNo, pageSize);
		IPage<AfterSaleInvoice> pageList = afterSaleInvoiceService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param afterSaleInvoice
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AfterSaleInvoice afterSaleInvoice) {
		afterSaleInvoiceService.save(afterSaleInvoice);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param afterSaleInvoice
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AfterSaleInvoice afterSaleInvoice) {
		AfterSale afterSale = new AfterSale();
		afterSale.setId(afterSaleInvoice.getAfterSaleId());
		afterSale.setStatus(Constants.STATUS_COMPLETE);
		afterSaleService.updateById(afterSale);
		return Result.ok("操作成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		afterSaleInvoiceService.removeById(id);
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
		this.afterSaleInvoiceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param afterSaleId
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="afterSaleId",required=true) String afterSaleId) {
		AfterSale afterSale = afterSaleService.getById(afterSaleId);
		LambdaQueryWrapper<AfterSaleInvoice> qw = new LambdaQueryWrapper<>();
		qw.eq(AfterSaleInvoice::getAfterSaleId,afterSaleId);
		AfterSaleInvoice afterSaleInvoice = afterSaleInvoiceService.getOne(qw);
		afterSaleInvoice.setAfterSaleNo(afterSale.getAfterSaleNo());
		if(Constants.STATUS_PROCESS.equals(afterSale.getStatus())){
			afterSaleInvoice.setStatusText("待处理");
		}else if(Constants.STATUS_COMPLETE.equals(afterSale.getStatus())){
			afterSaleInvoice.setStatusText("已完成");
		}else if(Constants.STATUS_CANCEL.equals(afterSale.getStatus())){
			afterSaleInvoice.setStatusText("客户取消");
		}
		return Result.ok(afterSaleInvoice);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param afterSaleInvoice
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, AfterSaleInvoice afterSaleInvoice) {
      return super.exportXls(request, afterSaleInvoice, AfterSaleInvoice.class, "售后开票明细");
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
      return super.importExcel(request, response, AfterSaleInvoice.class);
  }

}
