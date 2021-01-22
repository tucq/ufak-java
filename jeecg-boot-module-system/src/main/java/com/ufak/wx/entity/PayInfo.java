package com.ufak.wx.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2021/1/20.
 */
@Data
public class PayInfo {

    /**库存不足的商品名称，不为空则库存不足*/
    private String productNames;
    /**商品总金额*/
    private BigDecimal totalAmount;
    /**邮费*/
    private BigDecimal freightAmount;
    /**活动优惠券*/
    private BigDecimal eventAmount;
    /**优惠券*/
    private BigDecimal couponAmount;
    /**实际支付金额*/
    private BigDecimal paymentAmount;
    /**支付形式 0: 正常购买，1：秒杀购买*/
    private int buyType = 0;

    public BigDecimal getPaymentAmount() {
        return this.totalAmount.add(this.freightAmount).add(this.eventAmount).add(this.couponAmount);
    }

}
