package com.ufak.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.product.entity.ProductInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品信息
 * @Author: jeecg-boot
 * @Date:   2020-02-24
 * @Version: V1.0
 */
public interface IProductInfoService extends IService<ProductInfo> {

    IPage<ProductInfo> selectPage(Integer pageNo, Integer pageSize, Map paramMap);

    /**
     * 保存商品信息
     * @param productInfo
     */
    void saveProductInfo(ProductInfo productInfo);

    /**
     * 编辑商品信息
     * @param productInfo
     * @return
     */
    void updateProductInfo(ProductInfo productInfo);

    /**
     * 查询首页商品
     */
    List<ProductInfo> queryHomeProduct(Integer pageNo, Integer pageSize);

    /**
     * 查询首页广告关联商品
     */
    List<ProductInfo> queryAdsProduct(String adsId,Integer pageNo, Integer pageSize);



}
