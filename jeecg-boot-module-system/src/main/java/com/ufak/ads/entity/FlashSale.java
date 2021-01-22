package com.ufak.ads.entity;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 限制抢购
 * @Author: jeecg-boot
 * @Date:   2021-01-16
 * @Version: V1.0
 */
@Data
@TableName("t_flash_sale")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_flash_sale对象", description="限制抢购")
public class FlashSale {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private String id;
	/**商品id*/
	@Excel(name = "商品id", width = 15)
    @ApiModelProperty(value = "商品id")
	private String productId;
	/**标题*/
	@Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
	private String title;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private String remark;
	/**秒杀价*/
	@Excel(name = "秒杀价", width = 15)
    @ApiModelProperty(value = "秒杀价")
	private java.math.BigDecimal killPrice;
	/**库存*/
	@Excel(name = "库存", width = 15)
    @ApiModelProperty(value = "库存")
	private Integer stock;
	/**状态：0-启用，1-停用*/
	@Excel(name = "状态：0-启用，1-停用", width = 15)
    @ApiModelProperty(value = "状态：0-启用，1-停用")
	private String state;
	/**开始时间*/
	private String startTime;
	/**结束时间*/
	private String endTime;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;

	@TableField(exist = false)
	private String productName;
	@TableField(exist = false)
	private String specsImage;
	@TableField(exist = false)
	private String specsTitle;
	@TableField(exist = false)
	private BigDecimal price;
	@TableField(exist = false)
	private BigDecimal virtualPrice;

}
