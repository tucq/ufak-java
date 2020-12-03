package com.ufak.aftesale.entity;

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

/**
 * @Description: 售后开票明细
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Data
@TableName("t_after_sale_invoice")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_after_sale_invoice对象", description="售后开票明细")
public class AfterSaleInvoice {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**售后表id*/
	@Excel(name = "售后表id", width = 15)
    @ApiModelProperty(value = "售后表id")
	private java.lang.String afterSaleId;
	/**发票类型 0-电子发票，1-纸质发票*/
	@Excel(name = "发票类型 0-电子发票，1-纸质发票", width = 15)
    @ApiModelProperty(value = "发票类型 0-电子发票，1-纸质发票")
	private java.lang.String invoiceType;
	/**抬头类型，0-个人，1-单位*/
	@Excel(name = "抬头类型，0-个人，1-单位", width = 15)
    @ApiModelProperty(value = "抬头类型，0-个人，1-单位")
	private java.lang.String objectType;
	/**个人/单位名称*/
	@Excel(name = "个人/单位名称", width = 15)
    @ApiModelProperty(value = "个人/单位名称")
	private java.lang.String name;
	/**纳税识别号*/
	@Excel(name = "纳税识别号", width = 15)
    @ApiModelProperty(value = "纳税识别号")
	private java.lang.String taxNo;
	/**注册地址*/
	@Excel(name = "注册地址", width = 15)
    @ApiModelProperty(value = "注册地址")
	private java.lang.String registerAddr;
	/**注册电话*/
	@Excel(name = "注册电话", width = 15)
    @ApiModelProperty(value = "注册电话")
	private java.lang.String registerTel;
	/**开户行*/
	@Excel(name = "开户行", width = 15)
    @ApiModelProperty(value = "开户行")
	private java.lang.String bank;
	/**开户行帐号*/
	@Excel(name = "开户行帐号", width = 15)
    @ApiModelProperty(value = "开户行帐号")
	private java.lang.String bankAccount;
	private java.lang.String username;
	private java.lang.String telephone;

	/**电子发票地址*/
	@Excel(name = "电子发票地址", width = 15)
    @ApiModelProperty(value = "电子发票地址")
	private java.lang.Object images;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;

	/**
	 * 订单id
	 */
	@TableField(exist = false)
	private String orderId;
}
