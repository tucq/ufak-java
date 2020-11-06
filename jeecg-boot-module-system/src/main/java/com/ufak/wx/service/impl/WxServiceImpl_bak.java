//package com.ufak.wx.service.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.bian.common.core.domain.AjaxResult;
//import com.bian.common.utils.StringUtils;
//import com.bian.framework.config.jwt.AuthService;
//import com.bian.sales.entity.Constant;
//import com.bian.sales.entity.PayInfo;
//import com.bian.sales.service.IWxService;
//import com.bian.sales.util.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import com.thoughtworks.xstream.XStream;
//import org.springframework.http.HttpEntity;
//import org.slf4j.Logger;
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//
//import com.ufak.wx.service.IWxService;
//import org.springframework.stereotype.Service;
//
//@Service
//public class WxServiceImpl implements IWxService
//{
//    Logger logger = LoggerFactory.getLogger(WxServiceImpl.class);
//
//    @Override
//    public AjaxResult goWeChatPay(String brokerId, HttpServletRequest request) throws Exception {
//        String clientIP = CommonUtil.getClientIp(request);
//        logger.error("openId: " + brokerId + ", clientIP: " + clientIP);
//        String randomNonceStr = RandomUtils.generateMixString(32);
//        Map<String, String> result = unifiedOrder(brokerId, clientIP, randomNonceStr);
//        System.out.println(request.toString());
//        if(StringUtils.isBlank(result.get("prepay_id"))) {
//            return AjaxResult.error("支付错误");
//        } else {
//            logger.info("支付成功");
//            Map<String,Object> jsonObject = new LinkedHashMap();
//            String noncestr = RandomUtils.generateMixString(32);
//            String prepayid = result.get("prepay_id");
//            String timestamp = String.valueOf(new Date().getTime()/1000);
//            jsonObject.put("appid",Constant.APP_ID);
//            jsonObject.put("noncestr",noncestr);
//            jsonObject.put("package","Sign=WXPay");
//            jsonObject.put("partnerid",Constant.MCH_ID);
//            jsonObject.put("prepayid",result.get("prepay_id"));
//            jsonObject.put("timestamp",new Date().getTime()/1000);
//            jsonObject.put("sign",getSignforPayment(noncestr,prepayid,timestamp ));
//            return AjaxResult.success(jsonObject);
//        }
//    }
//
//    /**
//     * @Function: 去支付
//     * @author:   YangXueFeng
//     * @Date:     2019/6/14 16:50
//     */
//    /**
//     * 调用统一下单接口
//     * @param brokerId
//     */
//    private Map<String, String>
//           ss (String brokerId, String clientIP, String randomNonceStr) {
//
//    try {
//
//        //生成预支付交易单，返回正确的预支付交易会话标识后再在APP里面调起支付
//        String url = Constant.URL_UNIFIED_ORDER;
//
//        PayInfo payInfo = createPayInfo(brokerId, clientIP, randomNonceStr);
//        String md5 = getSign(payInfo);
//        payInfo.setSign(md5);
//
//        logger.error("md5 value: " + md5);
//
//        String xml = CommonUtil.payInfoToXML(payInfo);
//        xml = xml.replace("__", "_").replace("<![CDATA[1]]>", "1");
//        //xml = xml.replace("__", "_").replace("<![CDATA[", "").replace("]]>", "");
//        logger.error(xml);
//
//        StringBuffer buffer = HttpUtil.httpsRequest(url, "POST", xml);
//        logger.error("unifiedOrder request return body: \n" + buffer.toString());
//        Map<String, String> result = CommonUtil.parseXml(buffer.toString());
//
//
//        String return_code = result.get("return_code");
//        if(StringUtils.isNotBlank(return_code) && return_code.equals("SUCCESS")) {
//
//            String return_msg = result.get("return_msg");
//            if(StringUtils.isNotBlank(return_msg) && !return_msg.equals("OK")) {
//                logger.error("统一下单错误！");
//                return null;
//            }
//
//            String prepay_Id = result.get("prepay_id");
//            return result;
//
//        } else {
//            return null;
//        }
//
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//
//    return null;
//}
//
//    /**
//     * 数生成统一下单接口的请求参
//     * https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
//     * @param brokerId
//     * @param clientIP
//     * @param randomNonceStr
//     * @return
//     */
//    private PayInfo createPayInfo(String brokerId, String clientIP, String randomNonceStr) {
//        clientIP ="222.72.148.34";
//        Date date = new Date();
//        String timeStart = TimeUtils.getFormatTime(date, Constant.TIME_FORMAT);
//        String timeExpire = TimeUtils.getFormatTime(TimeUtils.addDay(date, Constant.TIME_EXPIRE), Constant.TIME_FORMAT);
//
//        String randomOrderId = CommonUtil.getRandomOrderId(); //生成随机商品订单号
//
//        PayInfo payInfo = new PayInfo();
//        payInfo.setAppid(Constant.APP_ID);
//        payInfo.setMch_id(Constant.MCH_ID);
//        payInfo.setDevice_info("WEB");
//        payInfo.setNonce_str(randomNonceStr);
//        payInfo.setSign_type("MD5");  //默认即为MD5
//        payInfo.setBody("必安glJSAPI支付测试");
//        payInfo.setAttach("支付测试4luluteam");
//        payInfo.setOut_trade_no(randomOrderId);
//        payInfo.setTotal_fee(1);
//        payInfo.setSpbill_create_ip(clientIP);
//        payInfo.setTime_start(timeStart);
//        payInfo.setTime_expire(timeExpire);
//        payInfo.setNotify_url(Constant.URL_NOTIFY);
//        payInfo.setTrade_type("APP");
//        payInfo.setLimit_pay("no_credit");
////        payInfo.setOpenid(brokerId);
//        return payInfo;
//    }
//
//    private String getSign(PayInfo payInfo) throws Exception {
//        StringBuffer sb = new StringBuffer();
//        sb.append("appid=" + payInfo.getAppid())
//                .append("&attach=" + payInfo.getAttach())
//                .append("&body=" + payInfo.getBody())
//                .append("&device_info=" + payInfo.getDevice_info())
//                .append("&limit_pay=" + payInfo.getLimit_pay())
//                .append("&mch_id=" + payInfo.getMch_id())
//                .append("&nonce_str=" + payInfo.getNonce_str())
//                .append("&notify_url=" + payInfo.getNotify_url())
////                .append("&openid=" + payInfo.getOpenid())
//                .append("&out_trade_no=" + payInfo.getOut_trade_no())
//                .append("&sign_type=" + payInfo.getSign_type())
//                .append("&spbill_create_ip=" + payInfo.getSpbill_create_ip())
//                .append("&time_expire=" + payInfo.getTime_expire())
//                .append("&time_start=" + payInfo.getTime_start())
//                .append("&total_fee=" + payInfo.getTotal_fee())
//                .append("&trade_type=" + payInfo.getTrade_type())
//                .append("&key=" + Constant.API_KEY);
//
//        System.out.println("排序后的拼接参数：" + sb.toString());
//        return CommonUtil.getMD5(sb.toString().trim()).toUpperCase();
//    }
//
//    private String getSignforPayment(String noncestr,String prepayid,String timestamp) throws Exception {
//        StringBuffer sb = new StringBuffer();
//        sb.append("appid=" +Constant.APP_ID)
//                .append("&noncestr=" + noncestr)
//                .append("&package=" + "Sign=WXPay")
//                .append("&partnerid=" + Constant.MCH_ID)
//                .append("&prepayid=" + prepayid)
//                .append("&timestamp=" + timestamp)
//                .append("&key=" + Constant.API_KEY);
//
//        System.out.println("排序后的拼接参数：" + sb.toString());
//        return CommonUtil.getMD5(sb.toString().trim()).toUpperCase();
//    }
//
//}
//
