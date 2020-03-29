package com.ufak.usr.entity;

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
 * @Description: 客户地址信息表
 * @Author: jeecg-boot
 * @Date:   2020-03-29
 * @Version: V1.0
 */
@Data
@TableName("t_user_address")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_user_address对象", description="客户地址信息表")
public class UserAddress {
    
	/**主键id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键id")
	private String id;
	/**帐户id*/
	@Excel(name = "帐户id", width = 15)
    @ApiModelProperty(value = "帐户id")
	private String userId;
	/**收货姓名*/
	@Excel(name = "收货姓名", width = 15)
    @ApiModelProperty(value = "收货姓名")
	private String username;
	/**收货电话号码*/
	@Excel(name = "收货电话号码", width = 15)
    @ApiModelProperty(value = "收货电话号码")
	private String telephone;
	/**地区*/
	@Excel(name = "地区", width = 15)
    @ApiModelProperty(value = "地区")
	private String address;
	/**详细地址*/
	@Excel(name = "详细地址", width = 15)
    @ApiModelProperty(value = "详细地址")
	private String detailAddress;
	/**是否默认 0-是,1-否*/
	@Excel(name = "是否默认 0-是,1-否", width = 15)
    @ApiModelProperty(value = "是否默认 0-是,1-否")
	private String defaule;
}
