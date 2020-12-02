package com.ufak.aftesale.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.entity.AfterSaleDetail;
import com.ufak.aftesale.mapper.AfterSaleDetailMapper;
import com.ufak.aftesale.service.IAfterSaleDetailService;
import com.ufak.aftesale.service.IAfterSaleService;
import com.ufak.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 退款/售后明细
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Service
public class AfterSaleDetailServiceImpl extends ServiceImpl<AfterSaleDetailMapper, AfterSaleDetail> implements IAfterSaleDetailService {
    @Autowired
    private IAfterSaleService afterSaleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void apply(JSONObject jsonObject) throws Exception {
        String userId = jsonObject.getString("userId");
        String orderId = jsonObject.getString("orderId");
        String serviceType = jsonObject.getString("serviceType");// 0：退款，1：退货，2：换货，3：维修，4：开票
        String orderDetailId = jsonObject.getString("orderDetailId");
        Integer applyNum = jsonObject.getInteger("applyNum");//申请数量
        String applyReason = jsonObject.getString("applyReason");//申请原因",//0-质量问题 ，1-其他
        String specificReason = jsonObject.getString("specificReason");//具体原因
        String returnType = jsonObject.getString("returnType");//返回方式 0-上门取件，1-快递至第三方卖家
        String addressType = jsonObject.getString("addressType");//是否已收货地址一致 0-是，1-否
        String address = jsonObject.getString("address");//所在地区
        String detailAddress = jsonObject.getString("detailAddress");
        String username = jsonObject.getString("username");//
        String telephone = jsonObject.getString("telephone");
        String images = jsonObject.getString("images");//上传图片路径，多个用逗号隔开

        AfterSale afterSale = new AfterSale();
        afterSale.setUserId(userId);
        afterSale.setOrderId(orderId);
        afterSale.setOrderDetailId(orderDetailId);
        afterSale.setServiceType(serviceType);
        afterSale.setStatus(Constants.STATUS_PROCESS);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String prefix = "";
        switch (serviceType){
            case "1":
                prefix = "TH";
                break;
            case "2":
                prefix = "HH";
                break;
            case "3":
                prefix = "WX";
                break;
        }
        String afterSaleNo = prefix + sdf.format(new Date()) + String.valueOf(Math.round((Math.random()+1) * 1000));//订单号
        afterSale.setAfterSaleNo(afterSaleNo);

        AfterSaleDetail detail = new AfterSaleDetail();
        detail.setApplyNum(applyNum);
        detail.setApplyReason(applyReason);
        detail.setSpecificReason(specificReason);
        detail.setReturnType(returnType);
        detail.setAddresstype(addressType);
        detail.setAddress(address);
        detail.setDetailAddress(detailAddress);
        detail.setUsername(username);
        detail.setTelephone(telephone);
        detail.setImages(images);

        afterSaleService.save(afterSale);
        detail.setAfterSaleId(afterSale.getId());
        this.save(detail);
    }
}
