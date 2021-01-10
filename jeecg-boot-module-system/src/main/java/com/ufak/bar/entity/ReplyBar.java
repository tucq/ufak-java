package com.ufak.bar.entity;

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

import java.util.Date;

/**
 * @Description: 贴吧回复
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Data
@TableName("t_reply_bar")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_reply_bar对象", description="贴吧回复")
public class ReplyBar {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private String id;
	/**贴吧id*/
	@Excel(name = "贴吧id", width = 15)
    @ApiModelProperty(value = "贴吧id")
	private String postBarId;
	/**内容*/
	@Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
	private Object content;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private String userId;
	/**真实姓名*/
	@Excel(name = "真实姓名", width = 15)
    @ApiModelProperty(value = "真实姓名")
	private String realname;
	/**头像*/
	@Excel(name = "头像", width = 15)
    @ApiModelProperty(value = "头像")
	private String avatar;
	/**点赞数量*/
	@Excel(name = "点赞数量", width = 15)
    @ApiModelProperty(value = "点赞数量")
	private Integer likesNum;
	/**踩数*/
	@Excel(name = "踩数", width = 15)
    @ApiModelProperty(value = "踩数")
	private Integer downNum;
	/**状态，0-显示，1-隐藏*/
	@Excel(name = "状态，0-显示，1-隐藏", width = 15)
    @ApiModelProperty(value = "状态，0-显示，1-隐藏")
	private String state;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 回复数量
	 */
	@TableField(exist = false)
	private Integer replyNum;
}
