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
import java.util.List;

/**
 * @Description: 贴吧
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Data
@TableName("t_post_bar")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_post_bar对象", description="贴吧")
public class PostBar {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private String id;
	/**分类id*/
	@Excel(name = "分类id", width = 15)
    @ApiModelProperty(value = "分类id")
	private String categoryId;
	/**标题*/
	@Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
	private String title;
	/**内容*/
	@Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
	private Object content;
	/**图片路径，多个逗号隔开*/
	@Excel(name = "图片路径，多个逗号隔开", width = 15)
    @ApiModelProperty(value = "图片路径，多个逗号隔开")
	private String images;
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
	/**分享数量*/
	@Excel(name = "分享数量", width = 15)
    @ApiModelProperty(value = "分享数量")
	private Integer shareNum;
	/**状态，0-显示，1-隐藏*/
	@Excel(name = "状态，0-显示，1-隐藏", width = 15)
    @ApiModelProperty(value = "状态，0-显示，1-隐藏")
	private String state;
	/**置顶顺序*/
	@Excel(name = "置顶顺序", width = 15)
    @ApiModelProperty(value = "置顶顺序")
	private Integer top;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**观注数量*/
	@TableField(exist = false)
	private Integer fansNum;
	/**回复数量*/
	@TableField(exist = false)
	private Integer replyNum;
	@TableField(exist = false)
	private Integer likesNum;
	@TableField(exist = false)
	private String categoryText;
	@TableField(exist = false)
	private String stateText;
//	@TableField(exist = false)
//	private IPage<ReplyBar> pageList;

	@TableField(exist = false)
	private boolean hasFans;
	@TableField(exist = false)
	private boolean hasLikes;
	@TableField(exist = false)
	private List<String> imageList;

}
