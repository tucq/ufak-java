package com.ufak.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.common.Constants;
import com.ufak.product.entity.ProductCategory;
import com.ufak.product.entity.ProductInfo;
import com.ufak.product.service.IProductCategoryService;
import com.ufak.product.service.IProductInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags="小程序分类API")
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private IProductCategoryService productCategoryService;
    @Autowired
    private IProductInfoService productInfoService;

    /**
     * 查找一级分类
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryHomeProductPage() {
        Result<List<ProductCategory>> result = new Result<>();
        QueryWrapper qw = new QueryWrapper();
//        qw.eq("pid","0");
        qw.orderByAsc("code");
        List<ProductCategory> list = productCategoryService.list(qw);
        result.setSuccess(true);
        result.setResult(list);
        return result;
    }

    @GetMapping(value = "/product/list")
    public Result<?> queryHomeProductPage(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                          HttpServletRequest req) {
        String categoryId = req.getParameter("categoryId");
        String orderByValue = req.getParameter("orderByValue");
        String productName = req.getParameter("productName");
        Map paramMap = new HashMap<>();
        if(StringUtils.isNotBlank(categoryId)){
            paramMap.put("categoryId",categoryId);
        }
        if(StringUtils.isNotBlank(orderByValue)){
            if(Constants.ORDERY_BY_XL.equals(orderByValue)){
                paramMap.put("salesVolume","desc");
            }else if(Constants.ORDERY_BY_DG.equals(orderByValue)){
                paramMap.put("price","asc");
            }else if(Constants.ORDERY_BY_GD.equals(orderByValue)){
                paramMap.put("price","desc");
            }
        }
        if(StringUtils.isNotBlank(productName)){    // 名称搜索
            paramMap.put("productName",productName);
        }

        IPage<ProductInfo> pageList = productInfoService.queryPhoneProductPage(pageNo,pageSize,paramMap);

        return Result.ok(pageList);
    }
}
