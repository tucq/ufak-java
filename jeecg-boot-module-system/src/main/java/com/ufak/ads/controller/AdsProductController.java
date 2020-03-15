package com.ufak.ads.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.ads.entity.AdsProduct;
import com.ufak.ads.service.IAdsProductService;
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
 * @Description: 广告关联商品信息
 * @Author: jeecg-boot
 * @Date:   2020-03-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags="广告关联商品信息")
@RestController
@RequestMapping("/adsProduct")
public class AdsProductController extends JeecgController<AdsProduct, IAdsProductService> {
	@Autowired
	private IAdsProductService adsProductService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "广告关联商品信息-分页列表查询")
	@ApiOperation(value="广告关联商品信息-分页列表查询", notes="广告关联商品信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String adsId = req.getParameter("adsId");
		Map paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(adsId)){
			paramMap.put("adsId",adsId);
		}
		IPage<AdsProduct> pageList = adsProductService.selectPage(pageNo,pageSize,paramMap);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param adsProduct
	 * @return
	 */
	@AutoLog(value = "广告关联商品信息-添加")
	@ApiOperation(value="广告关联商品信息-添加", notes="广告关联商品信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AdsProduct adsProduct) {
		adsProductService.save(adsProduct);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param adsProduct
	 * @return
	 */
	@AutoLog(value = "广告关联商品信息-编辑")
	@ApiOperation(value="广告关联商品信息-编辑", notes="广告关联商品信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AdsProduct adsProduct) {
		adsProductService.updateById(adsProduct);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告关联商品信息-通过id删除")
	@ApiOperation(value="广告关联商品信息-通过id删除", notes="广告关联商品信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		adsProductService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "广告关联商品信息-批量删除")
	@ApiOperation(value="广告关联商品信息-批量删除", notes="广告关联商品信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.adsProductService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "广告关联商品信息-通过id查询")
	@ApiOperation(value="广告关联商品信息-通过id查询", notes="广告关联商品信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AdsProduct adsProduct = adsProductService.getById(id);
		return Result.ok(adsProduct);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param adsProduct
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, AdsProduct adsProduct) {
      return super.exportXls(request, adsProduct, AdsProduct.class, "广告关联商品信息");
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
      return super.importExcel(request, response, AdsProduct.class);
  }

}
