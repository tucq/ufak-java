package com.ufak.aftesale.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.service.IAfterSaleService;
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
 * @Description: 退款/售后
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="退款/售后")
@RestController
@RequestMapping("/afterSale")
public class AfterSaleController extends JeecgController<AfterSale, IAfterSaleService> {
	@Autowired
	private IAfterSaleService afterSaleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param afterSale
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AfterSale afterSale,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String userId = req.getParameter("userId");
		String status = req.getParameter("status");
		Map paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(userId) && !"null".equals(userId)){
			paramMap.put("userId",userId);
		}
		if(StringUtils.isNotBlank(status) && !"null".equals(status)){
			paramMap.put("status",status);
		}
		IPage<AfterSale> pageList = afterSaleService.queryPageList(pageNo,pageSize,paramMap);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param afterSale
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AfterSale afterSale) {
		afterSaleService.save(afterSale);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param afterSale
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AfterSale afterSale) {
		afterSaleService.updateById(afterSale);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "退款/售后-通过id删除")
	@ApiOperation(value="退款/售后-通过id删除", notes="退款/售后-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		afterSaleService.removeById(id);
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
		this.afterSaleService.removeByIds(Arrays.asList(ids.split(",")));
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
		AfterSale afterSale = afterSaleService.getById(id);
		return Result.ok(afterSale);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param afterSale
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, AfterSale afterSale) {
      return super.exportXls(request, afterSale, AfterSale.class, "退款/售后");
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
      return super.importExcel(request, response, AfterSale.class);
  }

}
