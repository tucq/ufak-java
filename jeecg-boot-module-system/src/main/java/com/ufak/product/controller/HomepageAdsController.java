package com.ufak.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.common.FileUtil;
import com.ufak.product.entity.HomepageAds;
import com.ufak.product.service.IHomepageAdsService;
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
 * @Description: 首页广告
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
@Slf4j
@Api(tags="首页广告")
@RestController
@RequestMapping("/homepageAds")
public class HomepageAdsController extends JeecgController<HomepageAds, IHomepageAdsService> {
	@Autowired
	private IHomepageAdsService homepageAdsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param homepageAds
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "首页广告-分页列表查询")
	@ApiOperation(value="首页广告-分页列表查询", notes="首页广告-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HomepageAds homepageAds,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<HomepageAds> queryWrapper = QueryGenerator.initQueryWrapper(homepageAds, req.getParameterMap());
		queryWrapper.orderByAsc("sort");
		Page<HomepageAds> page = new Page<HomepageAds>(pageNo, pageSize);
		IPage<HomepageAds> pageList = homepageAdsService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param homepageAds
	 * @return
	 */
	@AutoLog(value = "首页广告-添加")
	@ApiOperation(value="首页广告-添加", notes="首页广告-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody HomepageAds homepageAds) {
		homepageAdsService.save(homepageAds);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param homepageAds
	 * @return
	 */
	@AutoLog(value = "首页广告-编辑")
	@ApiOperation(value="首页广告-编辑", notes="首页广告-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody HomepageAds homepageAds) {
		homepageAdsService.updateById(homepageAds);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "首页广告-通过id删除")
	@ApiOperation(value="首页广告-通过id删除", notes="首页广告-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		HomepageAds ads = homepageAdsService.getById(id);
		homepageAdsService.removeById(id);
		if(ads.getImgUrl() != null){
			FileUtil.delete(ads.getImgUrl());//删除文件
		}
		if(ads.getHeadImg() != null){
			FileUtil.delete(ads.getHeadImg());//删除文件
		}
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "首页广告-批量删除")
	@ApiOperation(value="首页广告-批量删除", notes="首页广告-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.homepageAdsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "首页广告-通过id查询")
	@ApiOperation(value="首页广告-通过id查询", notes="首页广告-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		HomepageAds homepageAds = homepageAdsService.getById(id);
		return Result.ok(homepageAds);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param homepageAds
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HomepageAds homepageAds) {
      return super.exportXls(request, homepageAds, HomepageAds.class, "首页广告");
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
      return super.importExcel(request, response, HomepageAds.class);
  }

}
