package com.ufak.bar.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.bar.entity.ReplyBar;

import java.util.Map;

/**
 * @Description: 贴吧回复
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
public interface IReplyBarService extends IService<ReplyBar> {


    IPage<ReplyBar> selectPage(Integer pageNo, Integer pageSize, Map paramMap);

}
