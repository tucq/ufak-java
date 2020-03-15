package com.ufak.ads.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.ads.entity.AdsProduct;

import java.util.Map;

/**
 * @Description: 广告关联商品信息
 * @Author: jeecg-boot
 * @Date:   2020-03-15
 * @Version: V1.0
 */
public interface IAdsProductService extends IService<AdsProduct> {
    IPage<AdsProduct> selectPage(Integer pageNo, Integer pageSize, Map paramMap);
}
