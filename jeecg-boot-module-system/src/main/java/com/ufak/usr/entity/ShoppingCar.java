package com.ufak.usr.entity;

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
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date:   2020-03-30
 * @Version: V1.0
 */
@Data
@TableName("t_shopping_car")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_shopping_car对象", description="购物车")
public class ShoppingCar {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private String userId;
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
	/**购买数量*/
	@Excel(name = "购买数量", width = 15)
    @ApiModelProperty(value = "购买数量")
	private Integer buyNum;
	@Excel(name = "是否选中", width = 15)
	@ApiModelProperty(value = "是否选中")
	private String isCheck;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**商品图片*/
	@TableField(exist = false)
	private String viewImage;
	@TableField(exist = false)
	private String productName;
	/**商品格式*/
	@TableField(exist = false)
	private String specsName;
	/**商品价格*/
	@TableField(exist = false)
	private String price;
	@TableField(exist = false)
	private String stock;
	/**运费*/
	@TableField(exist = false)
	private BigDecimal freightAmount;

}
