package com.ufak.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 商品规格
 * @Author: jeecg-boot
 * @Date:   2019-05-29
 * @Version: V1.0
 */
@Data
@TableName("t_product_specs")
public class ProductSpecs implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.UUID)
	private String id;
	/**商品id*/
	@Excel(name = "商品id", width = 15)
	private String productId;
	/**父级节点*/
	@Excel(name = "父级节点", width = 15)
	private String pid;
	/**规格描述*/
	@Excel(name = "规格描述", width = 15)
	private String specsTitle;
	@Excel(name = "规格图片", width = 15)
	@ApiModelProperty(value = "规格图片")
	private String specsImage;
	@Excel(name = "级别", width = 15)
	@ApiModelProperty(value = "级别")
	private String level;
	@Excel(name = "启停标识", width = 15)
	@ApiModelProperty(value = "启停标识")
	private String stats;
	@Excel(name = "排序", width = 15)
	@ApiModelProperty(value = "排序")
	private int sort;
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;

}
