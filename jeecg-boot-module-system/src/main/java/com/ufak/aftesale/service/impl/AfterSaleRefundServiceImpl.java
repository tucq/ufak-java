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

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public void apply(String orderId, AfterSaleRefund afterSaleRefund) throws Exception {
        QueryWrapper<AfterSale> qw = new QueryWrapper();
        qw.eq("order_id",orderId);
        qw.eq("status",Constants.STATUS_PROCESS);
        int i = afterSaleService.count(qw);
        if(i > 0){
            throw new JeecgBootException("该笔订单已申请过退款，福安康将在24小时内会为您处理");
        }
        QueryWrapper<AfterSale> qw2 = new QueryWrapper();
        qw2.eq("order_id",orderId);
        qw2.eq("status",Constants.STATUS_COMPLETE);
        int ii = afterSaleService.count(qw2);
        if(ii > 0){
            throw new JeecgBootException("该笔订单已退款成功，详情请查看[退款/售后]");
        }

        Order order = orderService.getById(orderId);
        AfterSale afterSale = new AfterSale();
        afterSale.setOrderId(orderId);
        afterSale.setUserId(order.getUserId());
        afterSale.setServiceType(Constants.AFTER_SALE_REFUND);
        afterSale.setStatus(Constants.STATUS_PROCESS);
        afterSale.setTransactionId(order.getTransactionId());
        afterSale.setTotalFee(order.getTotalFee());//微信订单金额(分)
        afterSale.setRefundFee(order.getCashFee());//退款金额
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String afterSaleNo = "TK"+sdf.format(new Date()) + String.valueOf(Math.round((Math.random()+1) * 1000));//订单号
        afterSale.setAfterSaleNo(afterSaleNo);
        afterSale.setRefundDesc(afterSaleRefund.getRefundReason());
        afterSaleService.save(afterSale);

        afterSaleRefund.setAfterSaleId(afterSale.getId());
        afterSaleRefund.setPaymentAmount(order.getPaymentAmount());
        this.save(afterSaleRefund);
    }
}
