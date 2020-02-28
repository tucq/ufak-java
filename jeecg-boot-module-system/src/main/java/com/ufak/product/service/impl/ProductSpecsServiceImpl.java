package com.ufak.product.service.impl;

import com.ufak.product.entity.ProductSpecs;
import com.ufak.product.mapper.ProductSpecsMapper;
import com.ufak.product.service.IProductSpecsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 商品规格
 * @Author: jeecg-boot
 * @Date:   2020-02-28
 * @Version: V1.0
 */
@Service
public class ProductSpecsServiceImpl extends ServiceImpl<ProductSpecsMapper, ProductSpecs> implements IProductSpecsService {

}
