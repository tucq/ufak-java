package com.ufak.wx.service;

import org.jeecg.common.api.vo.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2020/6/11.
 */
public interface IWxService {

//    public Result<?> goWeChatPay(String brokerId, HttpServletRequest request) throws Exception ;
    public Result<?> weChatPay(HttpServletRequest request) throws Exception ;
}
