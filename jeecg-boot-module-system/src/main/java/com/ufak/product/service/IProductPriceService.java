package com.ufak.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.product.entity.ProductPrice;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品价格库存
 * @Author: jeecg-boot
 * @Date:   2020-03-08
 * @Version: V1.0
 */
public interface IProductPriceService extends IService<ProductPrice> {

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param paramMap
     * @return
     */
    IPage<ProductPrice> selectPage(Integer pageNo, Integer pageSize, Map paramMap);


    /**
     * 手机端查询商品价格库存
     * @param productId
     * @return
     */
    List<ProductPrice> queryProductPrice(String productId);

    /**
     * 查询价格
     * @param productId
     * @param specs1Id
     * @param specs2Id
     * @return
     */
    ProductPrice getPrice(String productId,String specs1Id,String specs2Id);

}
