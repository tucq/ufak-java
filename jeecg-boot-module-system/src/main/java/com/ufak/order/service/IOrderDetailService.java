package com.ufak.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.order.entity.OrderDetail;

import java.util.List;

/**
 * @Description: 订单明细
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
public interface IOrderDetailService extends IService<OrderDetail> {

    /**
     * 根据订单id查找
     * @param orderId
     * @return
     */
    List<OrderDetail> queryByOrderId(String orderId);
}
