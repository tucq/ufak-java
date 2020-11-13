package com.ufak.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.order.entity.OrderDetail;
import com.ufak.order.mapper.OrderDetailMapper;
import com.ufak.order.service.IOrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @Description: 订单明细
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
