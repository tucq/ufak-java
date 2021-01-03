package com.ufak.aftesale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

/**
 * @Description: 退款明细
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Data
@TableName("t_after_sale_refund")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_after_sale_refund对象", description="退款明细")
public class AfterSaleRefund {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**售后表id*/
	@Excel(name = "售后表id", width = 15)
    @ApiModelProperty(value = "售后表id")
	private java.lang.String afterSaleId;
	/**微信退款单号*/
	@Excel(name = "微信退款单号", width = 15)
	@ApiModelProperty(value = "微信退款单号")
	private java.lang.String refundId;
	/**退款总金额*/
	@Excel(name = "退款总金额", width = 15)
    @ApiModelProperty(value = "退款总金额")
	private java.math.BigDecimal paymentAmount;
	/**退款原因*/
	@Excel(name = "退款原因", width = 15)
    @ApiModelProperty(value = "退款原因")
	private java.lang.String refundReason;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
	/**退款联系人*/
	@Excel(name = "退款联系人", width = 15)
    @ApiModelProperty(value = "退款联系人")
	private java.lang.String refundContact;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
	private java.lang.String refundTelephone;
	/**交易编号*/
	@Excel(name = "交易编号", width = 15)
    @ApiModelProperty(value = "交易编号")
	private java.lang.String transactionId;
	/**微信通知退款金额*/
	@Excel(name = "微信通知退款金额", width = 15)
	@ApiModelProperty(value = "微信通知退款金额")
	private Integer refundFee;
	/**微信退款状态*/
	@Excel(name = "微信退款状态", width = 15)
	@ApiModelProperty(value = "微信退款状态")
	private java.lang.String refundStatus;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;

}
