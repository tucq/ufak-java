package com.ufak.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.order.entity.Order;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 获取我的订单数
     * @param userId
     * @return total_fee 订单数
     */
    @Select("SELECT order_status,count(1) as total_fee from t_order where user_id=#{value} and order_status in ('0','1','2') GROUP BY order_status")
    List<Order> getMyOrderNum(String userId);
}
