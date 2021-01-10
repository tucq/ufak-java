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
 * @Description: 退款/售后明细
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Data
@TableName("t_after_sale_detail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_after_sale_detail对象", description="退款/售后明细")
public class AfterSaleDetail {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**售后表id*/
	@Excel(name = "售后表id", width = 15)
    @ApiModelProperty(value = "售后表id")
	private java.lang.String afterSaleId;
	/**申请数量*/
	@Excel(name = "申请数量", width = 15)
    @ApiModelProperty(value = "申请数量")
	private java.lang.Integer applyNum;
	/**退货金额*/
	@Excel(name = "退货金额", width = 15)
    @ApiModelProperty(value = "退货金额")
	private java.math.BigDecimal refundAmount;
	/**申请原因 0-质量问题 ，1-其他*/
	@Excel(name = "申请原因 0-质量问题 ，1-其他", width = 15)
    @ApiModelProperty(value = "申请原因 0-质量问题 ，1-其他")
	private java.lang.String applyReason;
	/**具体原因*/
	@Excel(name = "具体原因", width = 15)
    @ApiModelProperty(value = "具体原因")
	private java.lang.String specificReason;
	/**返回方式 0-上门取件，1-快递至第三方卖家*/
	@Excel(name = "返回方式 0-上门取件，1-快递至第三方卖家", width = 15)
    @ApiModelProperty(value = "返回方式 0-上门取件，1-快递至第三方卖家")
	private java.lang.String returnType;
	/**是否已收货地址一致 0-是，1-否*/
	@Excel(name = "是否已收货地址一致 0-是，1-否", width = 15)
    @ApiModelProperty(value = "是否已收货地址一致 0-是，1-否")
	private java.lang.String addresstype;
	/**地区*/
	@Excel(name = "地区", width = 15)
    @ApiModelProperty(value = "地区")
	private java.lang.String address;
	/**详细地址*/
	@Excel(name = "详细地址", width = 15)
    @ApiModelProperty(value = "详细地址")
	private java.lang.String detailAddress;
	/**收货姓名*/
	@Excel(name = "收货姓名", width = 15)
    @ApiModelProperty(value = "收货姓名")
	private java.lang.String username;
	/**收货电话号码*/
	@Excel(name = "收货电话号码", width = 15)
    @ApiModelProperty(value = "收货电话号码")
	private java.lang.String telephone;
	/**上传图片*/
	@Excel(name = "上传图片", width = 15)
    @ApiModelProperty(value = "上传图片")
	private java.lang.Object images;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;

	@TableField(exist = false)
	private java.lang.String afterSaleNo;
	@TableField(exist = false)
	private java.lang.String statusText;
}
