package com.ufak.ads.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.ads.entity.AdsProduct;

import java.util.List;
import java.util.Map;

/**
 * @Description: 广告关联商品信息
 * @Author: jeecg-boot
 * @Date: 2020-03-15
 * @Version: V1.0
 */
public interface AdsProductMapper extends BaseMapper<AdsProduct> {

    List<AdsProduct> selectPage(Map paramMap);

    long totalCount(Map paramMap);
}
