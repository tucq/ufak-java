package com.ufak.ads.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.ads.entity.AdsProduct;
import com.ufak.ads.mapper.AdsProductMapper;
import com.ufak.ads.service.IAdsProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 广告关联商品信息
 * @Author: jeecg-boot
 * @Date:   2020-03-15
 * @Version: V1.0
 */
@Service
public class AdsProductServiceImpl extends ServiceImpl<AdsProductMapper, AdsProduct> implements IAdsProductService {
    @Autowired
    private AdsProductMapper adsProductMapper;

    @Override
    public IPage<AdsProduct> selectPage(Integer pageNo, Integer pageSize, Map paramMap) {
        int start = (pageNo - 1) * pageSize;
        paramMap.put("start",start);
        paramMap.put("size",pageSize);
        List<AdsProduct> list = adsProductMapper.selectPage(paramMap);
        long totalCount = adsProductMapper.totalCount(paramMap);
        Page page = new Page(pageNo, pageSize);
        page.setRecords(list);
        page.setTotal(totalCount);
        return page;
    }
}
