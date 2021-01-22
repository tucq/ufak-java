package com.ufak.ads.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.ads.entity.FlashSale;

import java.util.Map;

/**
 * @Description: 限制抢购
 * @Author: jeecg-boot
 * @Date:   2021-01-16
 * @Version: V1.0
 */
public interface IFlashSaleService extends IService<FlashSale> {

    IPage<FlashSale> selectPage(Integer pageNo, Integer pageSize, Map paramMap);

}
