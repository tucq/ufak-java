package com.ufak.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.product.entity.ProductInfo;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 手机端商品列表
     * @param paramMap
     * @return
     */
    List<ProductInfo> queryPhoneProductPage(Map paramMap);
    long totalCountPhoneProduct(Map paramMap);

    /**
     * 首页轮播图,分类, 插拔广告关联商品查询
     * @param paramMap
     * @return
     */
    List<ProductInfo> queryAdsProductPage(Map paramMap);
    long totalCountAdsProduct(Map paramMap);

    /**
     * 手机端获取商品详细信息
     * @param productId
     * @return
     */
    ProductInfo getProductDetail(@Param("productId")String productId);


}
