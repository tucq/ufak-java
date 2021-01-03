package com.ufak.aftesale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.aftesale.entity.AfterSaleRefund;

/**
 * @Description: 退款明细
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
public interface IAfterSaleRefundService extends IService<AfterSaleRefund> {

    /**
     * 退款申请
     * @param orderId
     * @param afterSaleRefund
     */
    void apply(String orderId,AfterSaleRefund afterSaleRefund) throws Exception;

    /**
     * 取水退款申请
     * @param afterSaleId
     * @throws Exception
     */
    void cancel(String afterSaleId) throws Exception;

}