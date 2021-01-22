package com.ufak.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.common.Constants;
import com.ufak.product.entity.ProductPrice;
import com.ufak.product.entity.ProductSpecs;
import com.ufak.product.mapper.ProductSpecsMapper;
import com.ufak.product.service.IProductPriceService;
import com.ufak.product.service.IProductSpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 商品规格
 * @Author: jeecg-boot
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Service
public class ProductSpecsServiceImpl extends ServiceImpl<ProductSpecsMapper, ProductSpecs> implements IProductSpecsService {
    @Autowired
    private IProductPriceService productPriceService;

    @Override
    public List<ProductSpecs> getProductDefaultSpecs(String productId) {
        LambdaQueryWrapper<ProductPrice> qw = new LambdaQueryWrapper<>();
        qw.eq(ProductPrice::getProductId,productId);
        qw.eq(ProductPrice::getDefaultFlag, Constants.YES);
        ProductPrice productPrice =  productPriceService.getOne(qw);

        List<ProductSpecs> list = new ArrayList<>();
        if(productPrice == null){
            return list;
        }
        LambdaQueryWrapper<ProductSpecs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductSpecs::getId,productPrice.getSpecs1Id());
        queryWrapper.eq(ProductSpecs::getStats,Constants.YES);
        ProductSpecs oneSpecs = this.getOne(queryWrapper);
        list.add(oneSpecs);

        if(productPrice.getSpecs2Id() != null){
            LambdaQueryWrapper<ProductSpecs> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(ProductSpecs::getId,productPrice.getSpecs2Id());
            queryWrapper2.eq(ProductSpecs::getStats,Constants.YES);
            ProductSpecs twoSpecs = this.getOne(queryWrapper2);
            list.add(twoSpecs);
        }
        return list;
    }

}
