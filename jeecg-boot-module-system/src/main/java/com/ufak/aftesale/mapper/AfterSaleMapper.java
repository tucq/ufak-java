package com.ufak.aftesale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.aftesale.entity.AfterSale;

import java.util.List;
import java.util.Map;

/**
 * @Description: 退款/售后
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
public interface AfterSaleMapper extends BaseMapper<AfterSale> {

    List<AfterSale> queryPageList(Map paramMap);

    long totalCount(Map paramMap);

}
