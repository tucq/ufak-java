package com.ufak.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.order.entity.Order;

import java.util.List;
import java.util.Map;

/**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
public interface OrderMapper extends BaseMapper<Order> {

    List<Order> queryAppPageList(Map paramMap);

    long totalCount(Map paramMap);


    List<Order> queryBackPageList(Map paramMap);

    long totalBackCount(Map paramMap);

}
