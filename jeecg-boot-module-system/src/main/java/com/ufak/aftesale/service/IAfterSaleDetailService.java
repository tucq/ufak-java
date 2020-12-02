package com.ufak.aftesale.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.aftesale.entity.AfterSaleDetail;

/**
 * @Description: 退款/售后明细
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
public interface IAfterSaleDetailService extends IService<AfterSaleDetail> {
    /**
     * 退款/售后申请
     * @param jsonObject
     */
    void apply(JSONObject jsonObject) throws Exception;
}
