package com.ufak.order.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.order.entity.OrderUnpaid;
import com.ufak.order.mapper.OrderUnpaidMapper;
import com.ufak.order.service.IOrderUnpaidService;
import org.springframework.stereotype.Service;

/**
 * @Description: 未付款订单签名
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Service
public class OrderUnpaidServiceImpl extends ServiceImpl<OrderUnpaidMapper, OrderUnpaid> implements IOrderUnpaidService {

}
