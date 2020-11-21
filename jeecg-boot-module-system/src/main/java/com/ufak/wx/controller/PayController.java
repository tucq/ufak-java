package com.ufak.wx.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ufak.common.Constants;
import com.ufak.common.HttpRequestUtil;
import com.ufak.common.PayCommonUtil;
import com.ufak.order.entity.Order;
import com.ufak.order.service.IOrderService;
import com.ufak.product.entity.ProductPrice;
import com.ufak.product.service.IProductInfoService;
import com.ufak.product.service.IProductPriceService;
import com.ufak.usr.entity.ShoppingCar;
import com.ufak.usr.service.IShoppingCarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jdom.JDOMException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.IPUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2020/11/4.
 */
@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IProductPriceService productPriceService;
    @Autowired
    private IShoppingCarService shoppingCarService;
    @Autowired
    private IProductInfoService productInfoService;
    @Autowired
    private RedisUtil redisUtil;

    private String randomString = PayCommonUtil.getRandomString(32);


    /** 微信订单支付
     * @param request
     * @return
     */
    @RequestMapping(value = "/wxPay", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<?> wxpay(HttpServletRequest request) {
        String username = request.getParameter("username");
        String telephone = request.getParameter("telephone");
        String address = request.getParameter("address");
        String detailAddress = request.getParameter("detailAddress");
        BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));//订单总金额
        BigDecimal freightAmount = new BigDecimal(request.getParameter("freightAmount"));//运费
        BigDecimal eventAmount = new BigDecimal(request.getParameter("eventAmount"));// 活动优惠券 负数
        BigDecimal couponAmount = new BigDecimal(request.getParameter("couponAmount")); // 优惠券 负数
        String remark = request.getParameter("remark");
        String createTime = request.getParameter("createTime");
        String payInfo = request.getParameter("payInfo");// 通过购物车结算是为空，立即购买是不为空

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String trade_no = sdf.format(new Date()) + String.valueOf(Math.round((Math.random()+1) * 1000));//订单号
        String openId = JwtUtil.getUserNameByToken(request);
        String description = null;
        try {
            //福安康医疗用品支付
            description = new String("福安康医疗用品".getBytes("UTF-8"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        BigDecimal paymentAmount = totalAmount.add(freightAmount).add(eventAmount).add(couponAmount);//实付金额
        BigDecimal paymentAmount = new BigDecimal("0.01");// 开发测试使用1分钱
        Map<String, String> map = weixinPrePay(trade_no,paymentAmount,description,openId,IPUtils.getIpAddr(request));//预支付返回信息
        if("SUCCESS".equals(map.get("return_code"))){ //返回状态码
            if("SUCCESS".equals(map.get("result_code"))){ //业务结果
                String pnames = this.checkStock(payInfo);
                if(pnames.length() > 0){
                    return Result.error("非常抱歉！["+pnames+"]库存已不足！请查看其它规格或类似商品！");
                }

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

                //预支付成功，生成订单，移除购物车数据
                /***生成订单信息*/
                Order order = new Order();
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                order.setUserId(sysUser.getId());
                order.setOrderNo(trade_no);
                order.setTotalAmount(totalAmount);
                order.setFreightAmount(freightAmount);
                order.setEventAmount(eventAmount);
                order.setCouponAmount(couponAmount);
                order.setOrderStatus(Constants.WAIT_PAY);
                order.setUsername(username);
                order.setTelephone(telephone);
                order.setAddress(address);
                order.setDetailAddress(detailAddress);
                order.setRemark(remark);
                order.setCreateTime(new Date(Long.valueOf(createTime)));
                try {
                    orderService.submitOrder(order,payInfo);
                    finalpackage.put("orderId",order.getId());//保存成功后返回orderId
                    finalpackage.put("createTime",createTime);//返回创建时间，用于前端计算失效时间
                    return Result.ok(finalpackage);
                }catch (JeecgBootException e){
                    return Result.error(e.getMessage());// 乐观锁检测库存已被更改
                }catch (Exception e) {
                    log.error("生成订单信息异常：{}",e);
                    return Result.error("生成订单信息异常，请联系客服");
                }
            }else{
                return Result.error(map.get("err_code_des"));//失败结果
            }
        }else{
            return Result.error("签名失败：" + map.get("return_msg"));//预支付接口交易失败原因
        }
    }

    /**
     * 校验库存是否足够
     * @param payInfo
     * @return
     */
    private String checkStock(String payInfo){
        String productNames = "";
        if(StringUtils.isNotBlank(payInfo) && !"null".equals(payInfo)){
            //商品立即购买
            JSONObject json = JSONObject.parseObject(payInfo);
            String productId = json.getString("productId");
            String specs1Id = json.getString("specs1Id");
            String specs2Id = json.getString("specs2Id");
            String buyNum = json.getString("buyNum");
            ProductPrice pp = productPriceService.getPrice(productId,specs1Id,specs2Id);
            if(pp.getStock() < Integer.valueOf(buyNum)){
                productNames = productInfoService.getById(productId).getName();
            }
        }else{
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            List<ShoppingCar> carList = shoppingCarService.getPayforList(sysUser.getId());
            for(ShoppingCar car : carList){
                ProductPrice pp = productPriceService.getPrice(car.getProductId(),car.getSpecs1Id(),car.getSpecs2Id());
                if(pp.getStock() < car.getBuyNum()){
                    productNames = productNames + productInfoService.getById(car.getProductId()).getName() + "/";
                }
            }
            if(productNames.length() > 0){
                productNames = productNames.substring(0,productNames.lastIndexOf("/"));
            }
        }

        return productNames;
    }

   /**
     * 统一下单
     * 应用场景：商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再在APP里面调起支付。
     * @param trade_no 订单号
     * @param paymentAmount 支付金额
     * @param description 商品描述
     * @param openid
     * @return
     */
    public Map<String, String> weixinPrePay(String trade_no,BigDecimal paymentAmount,
                                            String description, String openid, String ipAddress) {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", Constants.WX_APPID);  //应用appid
        parameterMap.put("mch_id", Constants.MCH_ID);  //商户号
        //parameterMap.put("device_info", "WEB");
        parameterMap.put("nonce_str", randomString);
        parameterMap.put("body", description);
        parameterMap.put("out_trade_no", trade_no);
//        parameterMap.put("fee_type", "CNY");
        BigDecimal total = paymentAmount.multiply(new BigDecimal(100));  //接口中参数支付金额单位为【分】，参数值不能带小数，所以乘以100
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
            log.error("统一下单异常：{}",e);
        } catch (IOException e) {
            log.error("统一下单异常：{}",e);
        }
        return map;
    }


    /**
     * 微信支付通知回调，做好签名校验后给需要给微信返回应答
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    @RequestMapping(value = "/call/back/wxPay/url", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
        log.info("--------------微信支付回调开始----------");
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
            Map<String, String> params = PayCommonUtil.doXMLParse(resultXml);//微信支付通知结果信息返回
            outSteam.close();
            if(inStream != null){
                inStream.close();
            }

            if("SUCCESS".equals(params.get("return_code"))){ //微信支付通知返回状态码 （此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断）
                if("SUCCESS".equals(params.get("result_code"))){ //业务结果
                    if (PayCommonUtil.isTenpaySign(params)) {
                        log.info("wxNotify:微信支付----返回成功");
                        //微信支付签名验证成功
                        resXml = Constants.resSuccessXml;

                        String outTradeNo = params.get("out_trade_no");
                        String transactionId = params.get("transaction_id");
                        String sign = params.get("sign");
                        String totalFee = params.get("total_fee");
                        String cashFee = params.get("cash_fee");
                        String resultCode = params.get("result_code");

                        QueryWrapper<Order> query = new QueryWrapper<>();
                        query.eq("order_no",outTradeNo);
                        Order order = orderService.getOne(query);
                        if(Constants.WAIT_PAY.equals(order.getOrderStatus())){
                            // 只有待付款状态才需要进行更新订单状态
                            order.setOrderStatus(Constants.WAIT_SEND);//待发货
                            order.setTransactionId(transactionId);
                            order.setSign(sign);
                            order.setTotalFee(Integer.valueOf(totalFee));
                            order.setCashFee(Integer.valueOf(cashFee));
                            order.setResultCode(resultCode);
                            orderService.updateById(order);
                            redisUtil.del(Constants.ORDER_KEY_PREFIX + order.getId());//redis 移除订单key
                            log.info("wxNotify:微信支付成功回调----更新订单状态");
                        }else{
                            log.info("wxNotify:微信支付成功回调----该订单状态已更新，不做任务处理");
                        }
                    }else{
                        log.error("wxNotify:微信支付签名验证失败：" + params.get("err_code_des"));
                        resXml = Constants.resFailXml;
                    }
                }else{
                    log.error("wxNotify:支付回调失败（业务结果result_code失败）,错误信息：" + params.get("err_code_des"));
                    resXml = Constants.resFailXml;
                }
            }else{
                log.error("wxNotify:微信支付通信失败：" + params.get("return_msg"));
                resXml = Constants.resFailXml;
            }
        }catch (Exception e){
            log.error("wxNotify:微信支付回调业务处理异常：{}", e);
        }finally {
            try {
                // 处理业务完毕，返回微信信息否则微信会一直发送通知消息
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                log.error("wxNotify:支付回调发布异常：{}", e);
            }
        }

    }



}
