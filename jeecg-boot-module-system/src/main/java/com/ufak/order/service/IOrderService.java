package com.ufak.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.order.entity.Order;

/**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
public interface IOrderService extends IService<Order> {

   /**
     * 提交订单
     * @param order
     * @param payInfo 通过购物车结算时不空，立即购买是不为空
     */
    void submitOrder(Order order,String payInfo) throws Exception;

}
