package com.ufak.ads.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.ads.entity.HomepageAds;
import com.ufak.ads.mapper.HomepageAdsMapper;
import com.ufak.ads.service.IHomepageAdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 首页广告
 * @Author: jeecg-boot
 * @Date:   2020-03-09
 * @Version: V1.0
 */
@Service
public class HomepageAdsServiceImpl extends ServiceImpl<HomepageAdsMapper, HomepageAds> implements IHomepageAdsService {

    @Autowired
    private HomepageAdsMapper homepageAdsMapper;

    @Override
    public List<HomepageAds> queryAdsList() {
        return null;
    }
}
