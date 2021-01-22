package com.ufak.ads.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.ads.entity.FlashSale;

import java.util.List;
import java.util.Map;

/**
 * @Description: 限制抢购
 * @Author: jeecg-boot
 * @Date:   2021-01-16
 * @Version: V1.0
 */
public interface FlashSaleMapper extends BaseMapper<FlashSale> {

    List<FlashSale> selectPage(Map paramMap);

    long totalCount(Map paramMap);

}
