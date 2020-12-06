package com.ufak.wx.controller;

import com.ufak.common.ClientCustomSSL;
import com.ufak.common.Constants;
import com.ufak.common.PayCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.jdom.JDOMException;
import org.jeecg.common.api.vo.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信退款
 * Created by Administrator on 2020/11/26.
 */
@Slf4j
@RestController
@RequestMapping("/refund")
public class RefundController {

    private String randomString = PayCommonUtil.getRandomString(32);

    /** 微信订单退款
     * @param request
     * @return
     */
    @RequestMapping(value = "/wxRefund", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<?> wxRefund(HttpServletRequest request) {
        String transactionId = request.getParameter("transactionId");//微信支付订单号
        String outRefundNo = request.getParameter("afterSaleNo");//退款售后单号
        String totalFee = request.getParameter("totalFee");
        String refundFee = request.getParameter("refundFee");

        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", Constants.WX_APPID);  //应用appid
        parameterMap.put("mch_id", Constants.MCH_ID);  //商户号
        parameterMap.put("nonce_str", randomString);//随机字符串
        parameterMap.put("transaction_id", transactionId);
        parameterMap.put("out_refund_no", outRefundNo);//商户退款单号
        parameterMap.put("total_fee", totalFee);//订单金额
        parameterMap.put("refund_fee", refundFee);//退款金额
        parameterMap.put("notify_url", Constants.TK_NOTIFY_URL);
        String sign = PayCommonUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        try {
            String result = ClientCustomSSL.doRefund(Constants.REFUND_URL,requestXML);
            Map<String, String> map = PayCommonUtil.doXMLParse(result);
            if("SUCCESS".equals(map.get("return_code"))) { //返回状态码
                if ("SUCCESS".equals(map.get("result_code"))) {
                    // TODO 退款成功后，业务操作
                    System.out.println("微信支付订单号: " + map.get("transaction_id"));
                    System.out.println("商户订单号: " + map.get("out_trade_no"));
                    System.out.println("商户退款单号: " + map.get("out_refund_no"));
                    System.out.println("微信退款单号: " + map.get("refund_id"));
                    System.out.println("退款金额: " + map.get("refund_fee"));
                    System.out.println("标价金额: " + map.get("total_fee"));
                    System.out.println("现金支付金额: " + map.get("cash_fee"));
                    return Result.ok();
                }else{
                    log.error("退款失败：退款单号="+outRefundNo+", 原因："+map.get("err_code_des"));
                    return Result.error(map.get("err_code_des"));//失败结果
                }
            }else{
                log.error("签名失败：退款单号="+outRefundNo);
                return Result.error("签名失败：" + map.get("return_msg"));
            }
        } catch (JDOMException e) {
            log.error("微信退款异常：{}",e);
            return Result.error("微信退款异常:"+e.getMessage());
        } catch (IOException e) {
            log.error("微信退款异常1：{}",e);
            return Result.error("微信退款异常1:"+e.getMessage());
        } catch (Exception e) {
            log.error("微信退款异常2：{}",e);
            return Result.error("微信退款异常2:"+e.getMessage());
        }
    }


    /**
     * 微信退款通知
     * @param request
     * @param response
     * @throws IOException
     * @throws JDOMException
     */
    @RequestMapping(value = "/call/back/wxRefund/url", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
        String resXml = "";
        InputStream inStream;
        try{
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            String resultXml = new String(outSteam.toByteArray(), "utf-8");
            Map<String, String> params = PayCommonUtil.doXMLParse(resultXml);//微信退款通知返回信息
            outSteam.close();
            if(inStream != null){
                inStream.close();
            }

            if("SUCCESS".equals(params.get("return_code"))){ //微信支付通知返回状态码 （此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断）
                if("SUCCESS".equals(params.get("result_code"))){ //业务结果
                    if (PayCommonUtil.isTenpaySign(params)) {
                        //微信支付签名验证成功
                        resXml = Constants.resSuccessXml;

                        String appid = params.get("appid");
                        String mch_id = params.get("mch_id");
                        String nonce_str = params.get("nonce_str");
                        String req_info = params.get("req_info");

                        System.out.println("==========》》》"+appid);
                        System.out.println("==========》》》"+mch_id);
                        System.out.println("==========》》》"+nonce_str);
                        System.out.println("==========》》》"+req_info);

                    }else{
                        log.error("wxNotify:微信退款签名验证失败：" + params.get("err_code_des"));
                        resXml = Constants.resFailXml;
                    }
                }else{
                    log.error("wxNotify:退款回调失败（业务结果result_code失败）,错误信息：" + params.get("err_code_des"));
                    resXml = Constants.resFailXml;
                }
            }else{
                log.error("wxNotify:微信退款通信失败：" + params.get("return_msg"));
                resXml = Constants.resFailXml;
            }
        }catch (Exception e){
            log.error("wxNotify:微信退款回调业务处理异常：{}", e);
        }finally {
            try {
                // 处理业务完毕，返回微信信息否则微信会一直发送通知消息
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                log.error("wxNotify:退款回调发布异常：{}", e);
            }
        }
    }


}
