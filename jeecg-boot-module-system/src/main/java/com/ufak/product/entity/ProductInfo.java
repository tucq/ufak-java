package com.ufak.product.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 商品信息
 * @Author: jeecg-boot
 * @Date:   2020-02-24
 * @Version: V1.0
 */
@Data
@TableName("t_product_info")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_product_info对象", description="商品信息")
public class ProductInfo {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
	private String name;
	/**商品描述*/
	@Excel(name = "商品描述", width = 15)
    @ApiModelProperty(value = "商品描述")
	private String description;
	/**商品主图*/
	@Excel(name = "商品主图", width = 15)
    @ApiModelProperty(value = "商品主图")
	private String image;
	/**商品分类*/
	@Excel(name = "商品分类", width = 15)
    @ApiModelProperty(value = "商品分类")
	private String productType;
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
	private String specs;
	/**销量*/
	@Excel(name = "销量", width = 15)
    @ApiModelProperty(value = "销量")
	private Integer salesVolume;
	/**服务*/
	@Excel(name = "服务", width = 15)
    @ApiModelProperty(value = "服务")
	private String service;
	/**详情描述*/
	@Excel(name = "详情描述", width = 15)
    @ApiModelProperty(value = "详情描述")
	private Object detailDescribe;
	/**详情图片*/
	@Excel(name = "详情图片", width = 15)
    @ApiModelProperty(value = "详情图片")
	private String detailImages;
	/**上下架标识：0-上架，1-下架*/
	@Excel(name = "上下架标识：0-上架，1-下架", width = 15)
    @ApiModelProperty(value = "上下架标识：0-上架，1-下架")
	private String state;
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
}
