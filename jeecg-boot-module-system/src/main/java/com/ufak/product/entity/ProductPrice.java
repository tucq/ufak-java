package com.ufak.product.entity;

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

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 商品价格库存
 * @Author: jeecg-boot
 * @Date:   2020-03-08
 * @Version: V1.0
 */
@Data
@TableName("t_product_price")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_product_price对象", description="商品价格库存")
public class ProductPrice  implements Serializable {

	private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private String id;
	/**商品id*/
	@Excel(name = "商品id", width = 15)
    @ApiModelProperty(value = "商品id")
	private String productId;
	/**一级规格id*/
	@Excel(name = "一级规格id", width = 15)
    @ApiModelProperty(value = "一级规格id")
	private String specs1Id;
	/**二级规格id*/
	@Excel(name = "二级规格id", width = 15)
    @ApiModelProperty(value = "二级规格id")
	private String specs2Id;
	/**售卖价格*/
	@Excel(name = "售卖价格", width = 15)
    @ApiModelProperty(value = "售卖价格")
	private java.math.BigDecimal price;
	/**虚拟价格*/
	@Excel(name = "虚拟价格", width = 15)
    @ApiModelProperty(value = "虚拟价格")
	private java.math.BigDecimal virtualPrice;
	/**折扣*/
	@Excel(name = "折扣", width = 15)
    @ApiModelProperty(value = "折扣")
	private java.math.BigDecimal discount;
	/**库存*/
	@Excel(name = "库存", width = 15)
    @ApiModelProperty(value = "库存")
	private Integer stock;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**是否默认：0-是，1-否*/
	@Excel(name = "是否默认：0-是，1-否", width = 15)
    @ApiModelProperty(value = "是否默认：0-是，1-否")
	private String defaultFlag;
	@Excel(name = "排序", width = 15)
	@ApiModelProperty(value = "排序")
	private int sort;
	/**创建人员*/
	@Excel(name = "创建人员", width = 15)
    @ApiModelProperty(value = "创建人员")
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改人员*/
	@Excel(name = "修改人员", width = 15)
    @ApiModelProperty(value = "修改人员")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;

	/** 规格名称 */
	@TableField(exist = false)
	private String specsName;
	/** 规格图片 */
	@TableField(exist = false)
	private String specsImage;


}
