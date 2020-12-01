package com.ufak.wx.controller;

import com.ufak.common.Constants;
import com.ufak.common.HttpRequestUtil;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    /** 微信订单支付
     * @param request
     * @return
     */
    @RequestMapping(value = "/wxRefund", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<?> wxRefund(HttpServletRequest request) {
        String transactionId = request.getParameter("transactionId");//微信支付订单号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String out_trade_no = "TK"+sdf.format(new Date()) + String.valueOf(Math.round((Math.random()+1) * 1000));//订单号
        String totalFee = request.getParameter("totalFee");
        String refundFee = request.getParameter("refundFee");

        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", Constants.WX_APPID);  //应用appid
        parameterMap.put("mch_id", Constants.MCH_ID);  //商户号
        parameterMap.put("nonce_str", randomString);//随机字符串
        parameterMap.put("transaction_id", transactionId);
        parameterMap.put("out_trade_no", out_trade_no);//商户订单号
        parameterMap.put("total_fee", totalFee);//订单金额
        parameterMap.put("refund_fee", refundFee);//退款金额
        parameterMap.put("notify_url", Constants.TK_NOTIFY_URL);
        String sign = PayCommonUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        String result = HttpRequestUtil.sendPost(Constants.REFUND_URL,requestXML);
        Map<String, String> map = null;
        try {
            map = PayCommonUtil.doXMLParse(result);
            if("SUCCESS".equals(map.get("return_code"))) { //返回状态码
                if ("SUCCESS".equals(map.get("result_code"))) {
                    // TODO 退款成功后，业务操作
                    return Result.ok();
                }else{
                    return Result.error(map.get("err_code_des"));//失败结果
                }
            }else{
                return Result.error("签名失败：" + map.get("return_msg"));
            }
        } catch (JDOMException e) {
            log.error("微信退款异常：{}",e);
            return Result.ok("微信退款异常:"+e.getMessage());
        } catch (IOException e) {
            log.error("微信退款异常：{}",e);
            return Result.ok("微信退款异常:"+e.getMessage());
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

    }
}
