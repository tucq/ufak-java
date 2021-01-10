package com.ufak.bar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.bar.entity.ReplyBar;

import java.util.List;
import java.util.Map;

/**
 * @Description: 贴吧回复
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
public interface ReplyBarMapper extends BaseMapper<ReplyBar> {

    List<ReplyBar> selectPage(Map paramMap);

    long totalCount(Map paramMap);
}
