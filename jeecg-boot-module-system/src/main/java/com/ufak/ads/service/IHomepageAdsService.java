package com.ufak.ads.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.ads.entity.HomepageAds;

import java.util.List;

/**
 * @Description: 首页广告
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
public interface IHomepageAdsService extends IService<HomepageAds> {

    List<HomepageAds> queryAdsList();
}
