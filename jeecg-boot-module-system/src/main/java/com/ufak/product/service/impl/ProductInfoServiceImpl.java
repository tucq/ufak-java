package com.ufak.product.service.impl;

import com.ufak.product.entity.ProductInfo;
import com.ufak.product.mapper.ProductInfoMapper;
import com.ufak.product.service.IProductInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 商品信息
 * @Author: jeecg-boot
 * @Date:   2020-02-24
 * @Version: V1.0
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements IProductInfoService {

}
