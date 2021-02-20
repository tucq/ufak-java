package com.ufak.wx.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信内容安全验证接口服务
 * Created by Administrator on 2021/2/19.
 */
public interface IWxSecurityCheckService {


    /**
     * 文本内容校验
     * @param content
     * @return
     */
    JSONObject msgSecCheck(String content);

    /**
     * 图片内容校验
     * @param imagePath
     * @return
     */
    JSONObject imgSecCheck(String imagePath);
}
