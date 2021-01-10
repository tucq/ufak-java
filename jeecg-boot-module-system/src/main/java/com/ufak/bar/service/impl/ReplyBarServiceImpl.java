package com.ufak.bar.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.bar.entity.ReplyBar;
import com.ufak.bar.mapper.ReplyBarMapper;
import com.ufak.bar.service.IReplyBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 贴吧回复
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Service
public class ReplyBarServiceImpl extends ServiceImpl<ReplyBarMapper, ReplyBar> implements IReplyBarService {
    @Autowired
    private ReplyBarMapper replyBarMapper;

    @Override
    public IPage<ReplyBar> selectPage(Integer pageNo, Integer pageSize, Map paramMap) {
        int start = (pageNo - 1) * pageSize;
        paramMap.put("start",start);
        paramMap.put("size",pageSize);
        List<ReplyBar> list = replyBarMapper.selectPage(paramMap);
        long totalCount = replyBarMapper.totalCount(paramMap);
        Page page = new Page(pageNo, pageSize);
        page.setRecords(list);
        page.setTotal(totalCount);
        return page;
    }
}
