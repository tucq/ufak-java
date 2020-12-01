package com.ufak.aftesale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.entity.AfterSaleRefund;
import com.ufak.aftesale.mapper.AfterSaleRefundMapper;
import com.ufak.aftesale.service.IAfterSaleRefundService;
import com.ufak.aftesale.service.IAfterSaleService;
import com.ufak.common.Constants;
import com.ufak.order.entity.Order;
import com.ufak.order.service.IOrderService;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 退款明细
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Service
public class AfterSaleRefundServiceImpl extends ServiceImpl<AfterSaleRefundMapper, AfterSaleRefund> implements IAfterSaleRefundService {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IAfterSaleService afterSaleService;

    @Override
    public void apply(String orderDetailId, AfterSaleRefund afterSaleRefund) throws Exception {
        QueryWrapper<AfterSale> qw = new QueryWrapper();
        qw.eq("order_detail_id",orderDetailId);
        qw.eq("status",Constants.AFTER_SALE_REFUND);
        int i = afterSaleService.count(qw);
        if(i > 0){
            throw new JeecgBootException("该笔订单已申请退款，请误重复提交");
        }
        Order order = orderService.getById(orderId);
        AfterSale afterSale = new AfterSale();
        afterSale.setOrderId(orderId);
        afterSale.setUserId(order.getUserId());
        afterSale.setServiceType(Constants.AFTER_SALE_REFUND);
        afterSale.setStatus(Constants.STATUS_APPLY);
        afterSaleService.save(afterSale);

        afterSaleRefund.setAfterSaleId(afterSale.getId());
        afterSaleRefund.setPaymentAmount(order.getPaymentAmount());
        this.save(afterSaleRefund);
    }
}
