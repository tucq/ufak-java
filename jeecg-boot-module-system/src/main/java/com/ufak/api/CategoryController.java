package com.ufak.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ufak.product.entity.ProductCategory;
import com.ufak.product.service.IProductCategoryService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags="小程序分类API")
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private IProductCategoryService productCategoryService;

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
}
