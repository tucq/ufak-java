package com.ufak.api;

import com.ufak.product.entity.ProductInfo;
import com.ufak.product.service.IProductCategoryService;
import com.ufak.product.service.IProductInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags="小程序商品详细信息API")
@RestController
@RequestMapping("/api/product")
public class ProductDetailController {
    @Autowired
    private IProductCategoryService productCategoryService;
    @Autowired
    private IProductInfoService productInfoService;

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public Result<ProductInfo> getDictItems(@PathVariable String productId) {
        Result<ProductInfo> result = new Result<>();
        ProductInfo productInfo = productInfoService.getProductDetail(productId);
        result.setSuccess(true);
        result.setResult(productInfo);
        return result;
    }


}
