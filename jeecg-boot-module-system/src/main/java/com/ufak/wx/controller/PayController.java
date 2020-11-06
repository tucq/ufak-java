package com.ufak.wx.controller;

import com.ufak.common.Constants;
import com.ufak.common.HttpRequestUtil;
import com.ufak.common.PayCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.jdom.JDOMException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.IPUtils;
import org.jeecg.common.util.MD5Util;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2020/11/4.
 */
@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {

    private String randomString = PayCommonUtil.getRandomString(32);

    /**
     * @param request
     * @return
     */
    @RequestMapping(value = "/wxPay", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<?> wxpay(HttpServletRequest request) {
        BigDecimal totalAmount = new BigDecimal(request.getParameter("totalPrice"));
        String trade_no = request.getParameter("orderNum");
        String openId = JwtUtil.getUserNameByToken(request);
        String description = null;
        try {
            //福安康医疗用品支付
            description = new String("福安康医疗用品".getBytes("UTF-8"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, String> map = weixinPrePay(trade_no,totalAmount,description,openId,IPUtils.getIpAddr(request));
        if("SUCCESS".equals(map.get("return_code"))){//返回状态码
            if("SUCCESS".equals(map.get("result_code"))){//业务结果
                SortedMap<String, Object> finalpackage = new TreeMap<String, Object>();
                Long time = System.currentTimeMillis();
                //时间戳
                finalpackage.put("timeStamp", time.toString());
                //随机字符串
                finalpackage.put("nonceStr", map.get("nonce_str"));
                //统一下单接口返回的 prepay_id 参数值
                finalpackage.put("package", "prepay_id=" + map.get("prepay_id"));
                //签名算法
                finalpackage.put("signType", "MD5");

                StringBuffer sb = new StringBuffer();
                sb.append("appId=" + Constants.WX_APPID)
                        .append("&nonceStr=" + map.get("nonce_str"))
                        .append("&package=prepay_id=" + map.get("prepay_id"))
                        .append("&signType=MD5")
                        .append("&timeStamp=" + time.toString())
                        .append("&key=" + Constants.API_KEY);
                String paySign = MD5Util.MD5Encode(sb.toString().trim(),null);
                finalpackage.put("paySign", paySign);
                return Result.ok(finalpackage);
            }else{
                return Result.error(map.get("err_code_des"));//失败结果
            }
        }else{
            return Result.error("签名失败：" + map.get("return_msg"));//预支付接口交易失败原因
        }
    }

    /**
     * 统一下单
     * 应用场景：商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再在APP里面调起支付。
     * @param trade_no 订单号
     * @param totalAmount 支付金额
     * @param description 商品描述
     * @param openid
     * @return
     */
    public Map<String, String> weixinPrePay(String trade_no,BigDecimal totalAmount,
                                            String description, String openid, String ipAddress) {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", Constants.WX_APPID);  //应用appid
        parameterMap.put("mch_id", Constants.MCH_ID);  //商户号
        //parameterMap.put("device_info", "WEB");
        parameterMap.put("nonce_str", randomString);
        parameterMap.put("body", description);
        parameterMap.put("out_trade_no", trade_no);
//        parameterMap.put("fee_type", "CNY");
        BigDecimal total = totalAmount.multiply(new BigDecimal(100));  //接口中参数支付金额单位为【分】，参数值不能带小数，所以乘以100
        java.text.DecimalFormat df=new java.text.DecimalFormat("0");
        parameterMap.put("total_fee", df.format(total));
        parameterMap.put("spbill_create_ip", ipAddress);
        parameterMap.put("notify_url", Constants.NOTIFY_URL);
        parameterMap.put("trade_type", "JSAPI");//"JSAPI"
        //trade_type为JSAPI是 openid为必填项
        parameterMap.put("openid", openid);
        String sign = PayCommonUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        log.info("统一下单请求xml参数=" + requestXML);
        String result = HttpRequestUtil.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder",requestXML);
        log.info("统一下单返回xml参数=" + result);
        Map<String, String> map = null;
        try {
            map = PayCommonUtil.doXMLParse(result);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }



    /**
     * 此函数会被执行多次，如果支付状态已经修改为已支付，则下次再调的时候判断是否已经支付，如果已经支付了，则什么也执行
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    @RequestMapping(value = "/call/back/wxPay/url", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String notifyUrl(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
        log.info("--------------微信支付回调开始----------");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);
        outSteam.close();
        if(inStream != null){
            inStream.close();
        }
        for(String keyStr : params.keySet()){
            log.info("回调参数："+keyStr+" = " +params.get(keyStr));
        }


        Map<String,String> return_data = new HashMap<String,String>();
        if (!PayCommonUtil.isTenpaySign(params)) {
            // 支付失败
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "return_code不正确");
            return PayCommonUtil.getMapToXML(return_data);
        } else {
            log.info("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            // ------------------------------

            String total_fee = params.get("total_fee");
            double v = Double.valueOf(total_fee) / 100;
            String out_trade_no = String.valueOf(Long.parseLong(params.get("out_trade_no").split("O")[0]));
            Date accountTime = DateUtils.str2Date(params.get("time_end"), DateUtils.yyyymmddhhmmss);
            String ordertime = DateUtils.date2Str(new Date(), DateUtils.datetimeFormat);
            String totalAmount = String.valueOf(v);
            String appId = params.get("appid");
            String tradeNo = params.get("transaction_id");
            log.info("===============订单状态更新==============");
            return_data.put("return_code", "SUCCESS");
            return_data.put("return_msg", "OK");
            log.info("--------------微信支付回调开始结束----------");
            return PayCommonUtil.getMapToXML(return_data);
        }
    }



}
