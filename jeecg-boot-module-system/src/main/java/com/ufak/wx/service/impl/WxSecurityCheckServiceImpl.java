package com.ufak.wx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ufak.common.Constants;
import com.ufak.common.HttpRequestUtil;
import com.ufak.wx.service.IWxSecurityCheckService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2021/2/19.
 */
@Service
@Slf4j
public class WxSecurityCheckServiceImpl implements IWxSecurityCheckService{
    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    @Override
    public JSONObject msgSecCheck(String content) {
        String token = getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+token;
        JSONObject param = new JSONObject();
        param.put("content",content);
        String jsonStr = HttpRequestUtil.sendPost(url,param.toJSONString());
        return JSONObject.parseObject(jsonStr);
    }

    @Override
    public JSONObject imgSecCheck(String imagePath) {
        String token = getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token="+token;
        Map<String, String> fileMap = new HashedMap();
        fileMap.put("media", uploadpath + File.separator + imagePath);
        String jsonStr = HttpRequestUtil.formUpload(url,null,fileMap);
        return JSONObject.parseObject(jsonStr);
    }

    private String getAccessToken(){
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        String param = "grant_type=client_credential&appid="+ Constants.WX_APPID+"&secret="+Constants.WX_SECRET;
        String jsonStr = HttpRequestUtil.sendGet(url,param);
        return JSONObject.parseObject(jsonStr).getString("access_token");
    }
}
