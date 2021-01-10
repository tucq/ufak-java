package com.ufak.bar.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.bar.entity.PostBar;
import com.ufak.bar.entity.UserBar;

import java.util.Map;

/**
 * @Description: 贴吧
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
public interface IPostBarService extends IService<PostBar> {

    /**
     * 观注
     * @param userBar
     */
    void fans(UserBar userBar);

    /**
     * 取消观注
     * @param userBar
     */
    void cancelFans(UserBar userBar);

    IPage<PostBar> selectPage(Integer pageNo, Integer pageSize, Map paramMap);

}
