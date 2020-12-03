package com.ufak.aftesale.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.entity.AfterSaleInvoice;
import com.ufak.aftesale.mapper.AfterSaleInvoiceMapper;
import com.ufak.aftesale.service.IAfterSaleInvoiceService;
import com.ufak.aftesale.service.IAfterSaleService;
import com.ufak.common.Constants;
import com.ufak.order.entity.Order;
import com.ufak.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 售后开票明细
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Service
public class AfterSaleInvoiceServiceImpl extends ServiceImpl<AfterSaleInvoiceMapper, AfterSaleInvoice> implements IAfterSaleInvoiceService {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IAfterSaleService afterSaleService;

    @Override
    public void apply(AfterSaleInvoice afterSaleInvoice) throws Exception {
        Order order = orderService.getById(afterSaleInvoice.getOrderId());

        AfterSale afterSale = new AfterSale();
        afterSale.setUserId(order.getUserId());
        afterSale.setOrderId(order.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String afterSaleNo = "KP"+sdf.format(new Date()) + String.valueOf(Math.round((Math.random()+1) * 1000));//订单号
        afterSale.setAfterSaleNo(afterSaleNo);
        afterSale.setServiceType(Constants.AFTER_SALE_INVOICE);
        afterSale.setStatus(Constants.STATUS_PROCESS);
        afterSaleService.save(afterSale);

        afterSaleInvoice.setAfterSaleId(afterSale.getId());
        this.save(afterSaleInvoice);
    }
}
