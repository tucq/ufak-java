package com.ufak.usr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.usr.entity.UserProduct;
import com.ufak.usr.service.IUserProductService;
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
 * @Description: 用户收藏列表
 * @Author: jeecg-boot
 * @Date:   2020-12-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="用户收藏列表")
@RestController
@RequestMapping("/usr/userProduct")
public class UserProductController extends JeecgController<UserProduct, IUserProductService> {
	@Autowired
	private IUserProductService userProductService;
	
	/**
	 * 分页列表查询
	 *
	 * @param userProduct
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "用户收藏列表-分页列表查询")
	@ApiOperation(value="用户收藏列表-分页列表查询", notes="用户收藏列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(UserProduct userProduct,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<UserProduct> queryWrapper = QueryGenerator.initQueryWrapper(userProduct, req.getParameterMap());
		Page<UserProduct> page = new Page<UserProduct>(pageNo, pageSize);
		IPage<UserProduct> pageList = userProductService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param userProduct
	 * @return
	 */
	@AutoLog(value = "用户收藏列表-添加")
	@ApiOperation(value="用户收藏列表-添加", notes="用户收藏列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody UserProduct userProduct) {
		userProductService.save(userProduct);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param userProduct
	 * @return
	 */
	@AutoLog(value = "用户收藏列表-编辑")
	@ApiOperation(value="用户收藏列表-编辑", notes="用户收藏列表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody UserProduct userProduct) {
		userProductService.updateById(userProduct);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户收藏列表-通过id删除")
	@ApiOperation(value="用户收藏列表-通过id删除", notes="用户收藏列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		userProductService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户收藏列表-批量删除")
	@ApiOperation(value="用户收藏列表-批量删除", notes="用户收藏列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.userProductService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户收藏列表-通过id查询")
	@ApiOperation(value="用户收藏列表-通过id查询", notes="用户收藏列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		UserProduct userProduct = userProductService.getById(id);
		return Result.ok(userProduct);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param userProduct
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, UserProduct userProduct) {
      return super.exportXls(request, userProduct, UserProduct.class, "用户收藏列表");
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
      return super.importExcel(request, response, UserProduct.class);
  }

}
