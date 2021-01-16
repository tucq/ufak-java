package com.ufak.wx.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.entity.AfterSaleRefund;
import com.ufak.aftesale.service.IAfterSaleRefundService;
import com.ufak.aftesale.service.IAfterSaleService;
import com.ufak.common.ClientCustomSSL;
import com.ufak.common.Constants;
import com.ufak.common.PayCommonUtil;
import com.ufak.order.entity.Order;
import com.ufak.order.entity.OrderDetail;
import com.ufak.order.service.IOrderDetailService;
import com.ufak.order.service.IOrderService;
import com.ufak.product.service.IProductPriceService;
import lombok.extern.slf4j.Slf4j;
import org.jdom.JDOMException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.encryption.AesEncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
    @Autowired
    private IAfterSaleService afterSaleService;
    @Autowired
    private IAfterSaleRefundService afterSaleRefundService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IProductPriceService productPriceService;


    private String randomString = PayCommonUtil.getRandomString(32);

    /** 微信订单退款
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/wxRefund", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<?> wxRefund(@RequestBody JSONObject jsonObject) {
        String afterSaleId = jsonObject.getString("afterSaleId");//退款售后id
        AfterSale afterSale = afterSaleService.getById(afterSaleId);
        if(afterSale == null){
            return Result.error("退款ID="+afterSaleId+"不存在，请联系技术人员");
        }

        String transactionId = afterSale.getTransactionId();//微信支付订单号
        String outRefundNo = afterSale.getAfterSaleNo();//退款售后单号
        String totalFee = String.valueOf(afterSale.getTotalFee());
        String refundFee = String.valueOf(afterSale.getRefundFee());

        //退款校验
        QueryWrapper<Order> qryOrder = new QueryWrapper<>();
        qryOrder.eq("transaction_id",transactionId);
        Order order = orderService.getOne(qryOrder);
        if(order == null){
            return Result.error("微信交易单号不存在，请联系技术人员");
        }
        if(!order.getTotalFee().equals(Integer.valueOf(totalFee))){
            return Result.error("申请退款订单金额与下单支付订单金额不一致，请技术人员检查数据是否被篡改！");
        }
        if(!order.getCashFee().equals(Integer.valueOf(refundFee))){
            return Result.error("申请退款金额与下单支付现金金额不一致，请技术人员检查数据是否被篡改！");
        }

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
                    // 更新库存
                    String afterSaleNo = map.get("out_refund_no");//商户退款单号
                    String refund_fee = map.get("refund_fee");//退款金额
                    // TODO 扣除客户积分
                    log.info("商户退款单号: " + afterSaleNo);
                    log.info("退款金额: " + refund_fee);

                    // 返回库存， TODO 库存处理后期考虑 MQ
                    List<OrderDetail> orderDetails = orderDetailService.queryByOrderId(order.getId());
                    try{
                        for(OrderDetail detail: orderDetails){
                            productPriceService.returnStock(detail.getProductId(), detail.getSpecs1Id(), detail.getSpecs2Id(), detail.getBuyNum());
                        }
                        return Result.ok("退款成功，库存退回成功");
                    }catch (JeecgBootException e){
                        log.error("退款成功，库存更新失败，库存版本号已变更",e);
//                        return Result.error("系统繁忙，请稍后处理");
                        return Result.ok("退款成功，库存退回失败");
                    }catch (Exception e){
                        log.error("申请退款异常",e);
//                        return Result.error("申请退款异常，请联系技术人员");
                        return Result.ok("退款成功，库存退回异常");
                    }
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

            if("SUCCESS".equals(params.get("return_code"))){
                //微信支付签名验证成功
                resXml = Constants.resSuccessXml;
                String req_info = params.get("req_info");//返回的加密信息
                String xmlReqInfo = AesEncryptUtil.descrypt(req_info,Constants.API_KEY);//做AES-256-ECB解密（PKCS7Padding）
                Map<String, String> reqInfoMap = PayCommonUtil.doXMLParse(xmlReqInfo);
                String out_refund_no = reqInfoMap.get("out_refund_no");//退款单号

                QueryWrapper<AfterSale> query = new QueryWrapper<>();
                query.eq("after_sale_no",out_refund_no);
                AfterSale afterSale = afterSaleService.getOne(query);
                if(afterSale != null){
                    afterSale.setStatus(Constants.STATUS_COMPLETE);// 更新退款单状态
                    afterSaleService.updateById(afterSale);

                    LambdaQueryWrapper<AfterSaleRefund> qryRefund = new LambdaQueryWrapper<>();
                    qryRefund.eq(AfterSaleRefund::getAfterSaleId,afterSale.getId());
                    AfterSaleRefund afterSaleRefund = afterSaleRefundService.getOne(qryRefund);
                    afterSaleRefund.setRefundFee(Integer.valueOf(reqInfoMap.get("refund_fee")));
                    afterSaleRefund.setRefundStatus(reqInfoMap.get("refund_status"));
                    afterSaleRefundService.updateById(afterSaleRefund);// 更新明细

                    Order order = new Order();
                    order.setId(afterSale.getOrderId());
                    order.setOrderStatus(Constants.REFUND);// 更新订单状态为已退款
                    orderService.updateById(order);
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
