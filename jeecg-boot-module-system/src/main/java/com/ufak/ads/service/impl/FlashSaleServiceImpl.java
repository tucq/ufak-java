package com.ufak.ads.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.ads.entity.FlashSale;
import com.ufak.ads.mapper.FlashSaleMapper;
import com.ufak.ads.service.IFlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 限制抢购
 * @Author: jeecg-boot
 * @Date:   2021-01-16
 * @Version: V1.0
 */
@Service
public class FlashSaleServiceImpl extends ServiceImpl<FlashSaleMapper, FlashSale> implements IFlashSaleService {

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    @Override
    public IPage<FlashSale> selectPage(Integer pageNo, Integer pageSize, Map paramMap) {
        int start = (pageNo - 1) * pageSize;
        paramMap.put("start",start);
        paramMap.put("size",pageSize);
        List<FlashSale> list = flashSaleMapper.selectPage(paramMap);
        long totalCount = flashSaleMapper.totalCount(paramMap);
        Page page = new Page(pageNo, pageSize);
        page.setRecords(list);
        page.setTotal(totalCount);
        return page;
    }
}
