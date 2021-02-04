package com.ufak.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.product.entity.ProductSpecs;
import com.ufak.product.service.IProductSpecsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @Description: 商品规格
 * @Author: jeecg-boot
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags="商品规格")
@RestController
@RequestMapping("/product/specs")
public class ProductSpecsController extends JeecgController<ProductSpecs, IProductSpecsService> {
	@Autowired
	private IProductSpecsService productSpecsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param productSpecs
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ProductSpecs productSpecs,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProductSpecs> queryWrapper = QueryGenerator.initQueryWrapper(productSpecs, req.getParameterMap());
		String orderBy = req.getParameter("orderBy");
		if(StringUtils.isNotEmpty(orderBy)){
			queryWrapper.orderByAsc(orderBy);
		}
		Page<ProductSpecs> page = new Page<ProductSpecs>(pageNo, pageSize);
		IPage<ProductSpecs> pageList = productSpecsService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param productSpecs
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ProductSpecs productSpecs) {
		productSpecsService.save(productSpecs);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param productSpecs
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ProductSpecs productSpecs) {
		productSpecsService.updateById(productSpecs);
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
		productSpecsService.removeById(id);
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
		this.productSpecsService.removeByIds(Arrays.asList(ids.split(",")));
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
		ProductSpecs productSpecs = productSpecsService.getById(id);
		return Result.ok(productSpecs);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param productSpecs
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ProductSpecs productSpecs) {
      return super.exportXls(request, productSpecs, ProductSpecs.class, "商品规格");
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
      return super.importExcel(request, response, ProductSpecs.class);
  }

}
