package com.ufak.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.product.entity.ProductSpecs;

import java.util.List;

/**
 * @Description: 商品规格
 * @Author: jeecg-boot
 * @Date:   2020-02-28
 * @Version: V1.0
 */
public interface IProductSpecsService extends IService<ProductSpecs> {

    /**
     * 获取商品默认规格信息（包含一级，二级规格,二级规格可能没有）
     * @param productId
     * @return
     */
    List<ProductSpecs> getProductDefaultSpecs(String productId);

}
