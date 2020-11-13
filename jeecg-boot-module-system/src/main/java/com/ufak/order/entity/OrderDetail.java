package com.ufak.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 订单明细
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@Data
@TableName("t_order_detail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_order_detail对象", description="订单明细")
public class OrderDetail {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**订单id*/
	@Excel(name = "订单id", width = 15)
    @ApiModelProperty(value = "订单id")
	private java.lang.String orderId;
	/**商品id*/
	@Excel(name = "商品id", width = 15)
    @ApiModelProperty(value = "商品id")
	private java.lang.String productId;
	/**一级规格id*/
	@Excel(name = "一级规格id", width = 15)
    @ApiModelProperty(value = "一级规格id")
	private java.lang.String specs1Id;
	/**二级规格id*/
	@Excel(name = "二级规格id", width = 15)
    @ApiModelProperty(value = "二级规格id")
	private java.lang.String specs2Id;
	/**购买数量*/
	@Excel(name = "购买数量", width = 15)
    @ApiModelProperty(value = "购买数量")
	private java.lang.Integer buyNum;
	/**售卖价格*/
	@Excel(name = "售卖价格", width = 15)
    @ApiModelProperty(value = "售卖价格")
	private java.math.BigDecimal price;
}
