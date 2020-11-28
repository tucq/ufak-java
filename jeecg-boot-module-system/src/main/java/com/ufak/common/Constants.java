package com.ufak.common;

public class Constants {

    /**微信AppID 福安康*/
    public static final String WX_APPID = "wxe9e09343d62da01f";

    /**微信AppSecret*/
    public static final String WX_SECRET = "23c0bbda50b0f4e1f34cf84d351c64e0";

    /**微信支付商户号*/
    public static final String MCH_ID = "1580102971";

    /**微信支付APII密钥*/
    public static final String API_KEY = "12a0bbhua0b0f4e1fpicf84d351cfang";

    /**微信统一下单接口地址*/
    public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**微信退款接口地址*/
    public static final String REFUND_URL = " https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**支付回调通知地址*/
    public static final String NOTIFY_URL = "https://youfuankang.com/jeecg-boot/pay/call/back/wxPay/url";

    /**退款回调通知地址*/
    public static final String TK_NOTIFY_URL = "https://youfuankang.com/jeecg-boot/refund/call/back/wxRefund/url";

    /**返回微信支付通知成功状态码*/
    public static final String resSuccessXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    /**返回微信支付通知失败状态码*/
    public static final String resFailXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[ERROR]]></return_msg></xml>";

    public static final String ORDER_KEY_PREFIX = "OrderKey_";

    //是否标识：0-是，1-否
    public static final String YES = "0";
    public static final String NO = "1";

    // 商品规格级别
    public static final String LEVEL_ONE = "0";
    public static final String LEVEL_TWO = "1";

    /**轮播广告*/
    public static final String ADS_TYPE_HEAD = "0";
    /**首页分类*/
    public static final String ADS_TYPE_CATEGORY = "1";
    /**首页插播*/
    public static final String ADS_TYPE_INSERT = "2";

    /**商品列表排序*/
    public static final String ORDERY_BY_ZH = "0";//综合
    public static final String ORDERY_BY_XL = "1";//销量
    public static final String ORDERY_BY_DG = "2";//价格低到高
    public static final String ORDERY_BY_GD = "3";//价格高到低

    /**客户所属机构*/
    public static final String CLIENT_ORG_CODE = "A04";

    /**订单状态*/
    public static final String WAIT_PAY = "0";//待付款
    public static final String WAIT_SEND = "1";//待发货
    public static final String WAIT_RECEIVE = "2";//待收货
    public static final String CANCELLED = "3";//已取消
    public static final String COMPLETED = "4";//已完成







}
