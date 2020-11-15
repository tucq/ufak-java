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
 * @Description: 未付款订单签名
 * @Author: jeecg-boot
 * @Date:   2020-11-13
 * @Version: V1.0
 */
@Data
@TableName("t_order_unpaid")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_order_unpaid对象", description="未付款订单签名")
public class OrderUnpaid {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private String id;
	/**订单id*/
	@Excel(name = "订单id", width = 15)
    @ApiModelProperty(value = "订单id")
	private String orderId;
	/**时间戳*/
	@Excel(name = "时间戳", width = 15)
    @ApiModelProperty(value = "时间戳")
	private String timeStamp;
	/**随机字符串*/
	@Excel(name = "随机字符串", width = 15)
    @ApiModelProperty(value = "随机字符串")
	private String nonceStr;
	/**统一下单接口返回的 prepay_id参数值*/
	@Excel(name = "统一下单接口返回的 prepay_id参数值", width = 15)
    @ApiModelProperty(value = "统一下单接口返回的 prepay_id参数值")
	private String packageStr;
	/**签名算法*/
	@Excel(name = "签名算法", width = 15)
    @ApiModelProperty(value = "签名算法")
	private String signType;
	/**签名*/
	@Excel(name = "签名", width = 15)
    @ApiModelProperty(value = "签名")
	private String paySign;
}
