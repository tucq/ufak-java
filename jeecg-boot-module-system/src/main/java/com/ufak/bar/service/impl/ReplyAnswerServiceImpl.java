package com.ufak.bar.service.impl;

import com.ufak.bar.entity.ReplyAnswer;
import com.ufak.bar.mapper.ReplyAnswerMapper;
import com.ufak.bar.service.IReplyAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 回复交流
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Service
public class ReplyAnswerServiceImpl extends ServiceImpl<ReplyAnswerMapper, ReplyAnswer> implements IReplyAnswerService {

}
