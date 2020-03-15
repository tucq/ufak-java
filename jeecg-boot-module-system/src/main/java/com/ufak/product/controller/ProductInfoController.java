package com.ufak.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.common.FileUtil;
import com.ufak.product.entity.ProductCategory;
import com.ufak.product.entity.ProductInfo;
import com.ufak.product.entity.ProductSpecs;
import com.ufak.product.service.IProductCategoryService;
import com.ufak.product.service.IProductInfoService;
import com.ufak.product.service.IProductPriceService;
import com.ufak.product.service.IProductSpecsService;
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
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品信息
 * @Author: jeecg-boot
 * @Date:   2020-02-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags="商品信息")
@RestController
@RequestMapping("/product/info")
public class ProductInfoController extends JeecgController<ProductInfo, IProductInfoService> {
	@Autowired
	private IProductInfoService productInfoService;
	@Autowired
	private IProductSpecsService productSpecsService;
	@Autowired
	private IProductPriceService productPriceService;
	@Autowired
	private IProductCategoryService productCategoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "商品信息-分页列表查询")
	@ApiOperation(value="商品信息-分页列表查询", notes="商品信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String productName = req.getParameter("productName");
		String categoryCode = req.getParameter("categoryCode");
		String state = req.getParameter("state");
		Map paramMap = new HashMap<>();
		if(StringUtils.isNotBlank(productName)){
			paramMap.put("productName",productName);
		}
		if(StringUtils.isNotBlank(categoryCode)){
			paramMap.put("categoryCode",categoryCode);
		}
		if(StringUtils.isNotBlank(state)){
			paramMap.put("state",state);
		}

		IPage<ProductInfo> pageList = productInfoService.selectPage(pageNo,pageSize,paramMap);

		List<ProductInfo> records = pageList.getRecords();
		for(ProductInfo product : records){
			String code = product.getCategoryCode();
			List<ProductCategory> pcList = productCategoryService.queryParentByCode(code);
			String productTypeName = "";
			for(ProductCategory pc : pcList){
				productTypeName += pc.getName() + "/";
			}
			product.setProductTypeName(productTypeName.substring(0,productTypeName.length() - 1));
		}
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param productInfo
	 * @return
	 */
	@AutoLog(value = "商品信息-添加")
	@ApiOperation(value="商品信息-添加", notes="商品信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ProductInfo productInfo) {
		productInfoService.saveProductInfo(productInfo);
		Result<Object> result = Result.ok("添加成功！");
		result.setResult(productInfo.getId());
		return result;
	}
	
	/**
	 * 编辑
	 *
	 * @param productInfo
	 * @return
	 */
	@AutoLog(value = "商品信息-编辑")
	@ApiOperation(value="商品信息-编辑", notes="商品信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ProductInfo productInfo) {
		productInfoService.updateProductInfo(productInfo);
		Result<Object> result = Result.ok("编辑成功！");
		result.setResult(productInfo.getId());
		return result;
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商品信息-通过id删除")
	@ApiOperation(value="商品信息-通过id删除", notes="商品信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		this.deleteProduct(id);
		return Result.ok("删除成功!");
	}

	private void deleteProduct(String id){
		ProductInfo pi = productInfoService.getById(id);
		if(pi != null){
			String url = pi.getImage() + pi.getDetailImages();
			String[] urls = url.split(",");
			for(int i = 0;i < urls.length; i++){
				FileUtil.delete(urls[i]);// 删除图片
			}
			productInfoService.removeById(id);

			QueryWrapper rwSpecs = new QueryWrapper();
			rwSpecs.eq("product_id",id);
			List<ProductSpecs> specsList = productSpecsService.list(rwSpecs);
			for(ProductSpecs ps: specsList){
				FileUtil.delete(ps.getSpecsImage());
			}
			productSpecsService.remove(rwSpecs);

			QueryWrapper rwPrice = new QueryWrapper();
			rwPrice.eq("product_id",id);
			productPriceService.remove(rwPrice);
		}
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "商品信息-批量删除")
	@ApiOperation(value="商品信息-批量删除", notes="商品信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		for(String id : list){
			this.deleteProduct(id);
		}
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "商品信息-通过id查询")
	@ApiOperation(value="商品信息-通过id查询", notes="商品信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ProductInfo productInfo = productInfoService.getById(id);
		return Result.ok(productInfo);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param productInfo
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ProductInfo productInfo) {
      return super.exportXls(request, productInfo, ProductInfo.class, "商品信息");
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
      return super.importExcel(request, response, ProductInfo.class);
  }

}
