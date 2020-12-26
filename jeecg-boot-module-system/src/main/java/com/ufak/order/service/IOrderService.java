package com.ufak.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.order.entity.Order;

import java.util.Map;

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

   /**
    * 后端分页查找订单
    * @param pageNo
    * @param pageSize
    * @param paramMap
    * @return
    */
   IPage<Order> queryPageList(Integer pageNo, Integer pageSize, Map paramMap);


    /**
     * APP端分页查找订单
     * @param pageNo
     * @param pageSize
     * @param paramMap
     * @return
     */
    IPage<Order> queryAppPageList(Integer pageNo, Integer pageSize, Map paramMap);

    /**
     * 订单取消
     * @param orderId
     */
    void cancelOrder(String orderId) throws Exception;

    /**
     * 删除订单
     * @param orderId
     * @throws Exception
     */
    void deleteOrder(String orderId) throws Exception;

    /**
     * 再次购买
     * @param orderId
     * @throws Exception
     */
    void buyAgain(String orderId) throws Exception;

}
