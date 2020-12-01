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
 * @Description: 退款/售后
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Data
@TableName("t_after_sale")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_after_sale对象", description="退款/售后")
public class AfterSale {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private java.lang.String userId;
//	/**订单id*/
//	@Excel(name = "订单id", width = 15)
//    @ApiModelProperty(value = "订单id")
//	private java.lang.String orderId;
	/**订单明细id*/
	@Excel(name = "订单明细id", width = 15)
	@ApiModelProperty(value = "订单明细id")
	private java.lang.String orderDetailId;
	/**服务类型0：退款，1：退货，2：换货，3：维修，4：开票*/
	@Excel(name = "服务类型0：退款，1：退货，2：换货，3：维修，4：开票", width = 15)
    @ApiModelProperty(value = "服务类型0：退款，1：退货，2：换货，3：维修，4：开票")
	private java.lang.String serviceType;
	/**状态 0：提交申请，1：处理中，2：完成，3-取消*/
	@Excel(name = "状态 0：提交申请，1：处理中，2：完成，3-取消", width = 15)
    @ApiModelProperty(value = "状态 0：提交申请，1：处理中，2：完成，3-取消")
	private java.lang.String status;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
}
