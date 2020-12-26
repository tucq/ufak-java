package com.ufak.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@Data
@TableName("t_order")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_order对象", description="订单主表")
public class Order {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private java.lang.String userId;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
    @ApiModelProperty(value = "订单编号")
	private java.lang.String orderNo;
	/**物流编号*/
	@Excel(name = "物流编号", width = 15)
    @ApiModelProperty(value = "物流编号")
	private java.lang.String logisticsNo;
	/**订单金额*/
	@Excel(name = "订单金额", width = 15)
    @ApiModelProperty(value = "订单金额")
	private java.math.BigDecimal totalAmount;
	/**运费*/
	@Excel(name = "运费", width = 15)
	@ApiModelProperty(value = "运费")
	private java.math.BigDecimal freightAmount;
	/**活动优惠券*/
	@Excel(name = "活动优惠券", width = 15)
    @ApiModelProperty(value = "活动优惠券")
	private java.math.BigDecimal eventAmount;
	/**优惠券*/
	@Excel(name = "优惠券", width = 15)
    @ApiModelProperty(value = "优惠券")
	private java.math.BigDecimal couponAmount;
	/**实付金额*/
	private java.math.BigDecimal paymentAmount;
	/**订单状态: 0-待付款,1-待发货,2-待收货,3-已取消,4-已完成*/
	@Excel(name = "订单状态: 0-待付款,1-待发货,2-待收货,3-已取消,4-已完成", width = 15)
    @ApiModelProperty(value = "订单状态: 0-待付款,1-待发货,2-待收货,3-已取消,4-已完成")
	private java.lang.String orderStatus;
	/**收货姓名*/
	@Excel(name = "收货姓名", width = 15)
    @ApiModelProperty(value = "收货姓名")
	private java.lang.String username;
	/**收货电话号码*/
	@Excel(name = "收货电话号码", width = 15)
    @ApiModelProperty(value = "收货电话号码")
	private java.lang.String telephone;
	/**地区*/
	@Excel(name = "地区", width = 15)
    @ApiModelProperty(value = "地区")
	private java.lang.String address;
	/**详细地址*/
	@Excel(name = "详细地址", width = 15)
    @ApiModelProperty(value = "详细地址")
	private java.lang.String detailAddress;
	@Excel(name = "订单备注", width = 15)
	@ApiModelProperty(value = "订单备注")
	private java.lang.String remark;
	/**微信支付订单号*/
	@Excel(name = "微信支付订单号", width = 15)
    @ApiModelProperty(value = "微信支付订单号")
	private java.lang.String transactionId;
	/**微信支付回调签名*/
	@Excel(name = "微信支付回调签名", width = 15)
    @ApiModelProperty(value = "微信支付回调签名")
	private java.lang.String sign;
	/**微信订单金额(分)*/
	@Excel(name = "微信订单金额(分)", width = 15)
    @ApiModelProperty(value = "微信订单金额(分)")
	private java.lang.Integer totalFee;
	/**微信现金支付金额(分)*/
	@Excel(name = "微信现金支付金额(分)", width = 15)
    @ApiModelProperty(value = "微信现金支付金额(分)")
	private java.lang.Integer cashFee;
	/**微信支付回调业务结果*/
	@Excel(name = "微信支付回调业务结果", width = 15)
    @ApiModelProperty(value = "微信支付回调业务结果")
	private java.lang.String resultCode;
	/**物流到货时间*/
	@Excel(name = "物流到货时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "物流到货时间")
	private java.util.Date logisticsTime;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;

//	/**实付金额*/
//	@TableField(exist = false)
//	private java.math.BigDecimal payAmount;
	/**订单明细*/
	@TableField(exist = false)
	private List<OrderDetail> orderDetails;
	/**收货人/电话*/
	@TableField(exist = false)
	private java.lang.String usernameTel;
	/**收货地址/详情地址*/
	@TableField(exist = false)
	private java.lang.String userAddress;

	public List<OrderDetail> getOrderDetails() {
		if(orderDetails == null){
			orderDetails = new ArrayList<>();
		}
		return orderDetails;
	}

}
