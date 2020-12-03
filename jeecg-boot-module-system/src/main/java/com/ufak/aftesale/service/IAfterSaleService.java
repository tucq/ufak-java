package com.ufak.aftesale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.aftesale.entity.AfterSale;

import java.util.Map;

/**
 * @Description: 退款/售后
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
public interface IAfterSaleService extends IService<AfterSale> {

    /**
     * @param pageNo
     * @param pageSize
     * @param paramMap
     * @return
     */
    IPage<AfterSale> queryPageList(Integer pageNo, Integer pageSize, Map paramMap);
}
