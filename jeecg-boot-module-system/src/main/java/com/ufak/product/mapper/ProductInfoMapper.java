package com.ufak.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.product.entity.ProductInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品信息
 * @Author: jeecg-boot
 * @Date:   2020-02-24
 * @Version: V1.0
 */
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {

    List<ProductInfo> selectPage(Map paramMap);

    long totalCount(Map paramMap);

    List<ProductInfo> queryHomeProduct(Map paramMap);

    List<ProductInfo> queryAdsProduct(Map paramMap);
}
