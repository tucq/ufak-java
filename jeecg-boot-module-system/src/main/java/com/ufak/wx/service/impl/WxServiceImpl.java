package com.ufak.wx.service.impl;

import com.ufak.common.Constants;
import com.ufak.wx.service.IWxService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.IPUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.UUIDGenerator;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2020/11/4.
 */
@Service
public class WxServiceImpl implements IWxService {
    @Override
    public Result<?> weChatPay(HttpServletRequest request) throws Exception {
        String appid = Constants.WX_APPID;//小程序ID
        String body = "福安康医疗用品支付";//商品描述
        String mch_id = Constants.MCH_ID;//商户号
        String nonce_str = UUIDGenerator.generate();//32位UUID
        String notify_url = Constants.NOTIFY_URL;//通知地址
        String out_trade_no = String.valueOf(System.currentTimeMillis()) + String.valueOf(Math.round((Math.random()+1) * 1000));//商户订单号
        String spbill_create_ip = IPUtils.getIpAddr(request);//终端IP
        int total_fee = 1;//标价金额,单位为分
        String trade_type = "JSAPI";

        StringBuffer sb = new StringBuffer();
        sb.append("appid=" + appid)
        .append("&body=" + body)
        .append("&mch_id=" + mch_id)
        .append("&nonce_str=" + nonce_str)
        .append("&notify_url=" + notify_url)
        .append("&out_trade_no=" + out_trade_no)
        .append("&spbill_create_ip=" + spbill_create_ip)
        .append("&total_fee=" + total_fee)
        .append("&trade_type=" + trade_type);
        String stringA = sb.toString().trim();

        String stringSignTemp = stringA+"&key=" + Constants.WX_SECRET; //注：key为商户平台设置的密钥key
        String sign= MD5Util.MD5Encode(stringSignTemp,null).toUpperCase();

        StringBuffer xml = new StringBuffer();
        xml.append("<xml>")
                .append("appid")
                .append("")
                .append("")
                .append("")
                .append("")
                .append("")
                .append("")
                .append("")
                .append("")
                .append("")
                .append("</xml>");


//        String nonce_str =
        return null;
    }


}
