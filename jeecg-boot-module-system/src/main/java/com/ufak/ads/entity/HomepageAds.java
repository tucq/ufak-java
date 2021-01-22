package com.ufak.ads.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 首页广告
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
@Data
@TableName("t_homepage_ads")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="t_homepage_ads对象", description="首页广告")
public class HomepageAds implements Serializable {
	private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
	private String id;
	/**广告名称*/
	@Excel(name = "广告名称", width = 15)
	@ApiModelProperty(value = "广告名称")
	private String adsName;
	/**图片*/
	@Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
	private String imgUrl;
	/**类型: 0-轮播图, 1-广告图*/
	@Excel(name = "类型: 0-轮播图, 1-广告图", width = 15,dicCode="ads_type")
    @ApiModelProperty(value = "类型: 0-轮播图, 1-广告图")
	@Dict(dicCode = "ads_type")
	private String type;
	/**头部广告*/
	@Excel(name = "头部广告", width = 15)
    @ApiModelProperty(value = "头部广告")
	private String headImg;
	/**背景色*/
	@Excel(name = "背景色", width = 15)
    @ApiModelProperty(value = "背景色")
	private String bgColor;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private Integer sort;
	/**状态: 0-启用, 1-禁用*/
	@Excel(name = "状态: 0-启用, 1-禁用", width = 15)
    @ApiModelProperty(value = "状态: 0-启用, 1-禁用")
	@Dict(dicCode = "stats")
	private String state;
	/**是否列表: 0-是, 1-否*/
	@Excel(name = "是否列表: 0-是, 1-否", width = 15)
	@ApiModelProperty(value = "是否列表: 0-是, 1-否")
	@Dict(dicCode = "stats")
	private String isList;
	/**页面跳转路径*/
	private String pagePath;
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
