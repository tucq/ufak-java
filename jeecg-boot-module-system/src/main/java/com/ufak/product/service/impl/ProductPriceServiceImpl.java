package com.ufak.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.product.entity.ProductPrice;
import com.ufak.product.mapper.ProductPriceMapper;
import com.ufak.product.service.IProductPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品价格库存
 * @Author: jeecg-boot
 * @Date:   2020-03-08
 * @Version: V1.0
 */
@Service
public class ProductPriceServiceImpl extends ServiceImpl<ProductPriceMapper, ProductPrice> implements IProductPriceService {
    @Autowired
    private ProductPriceMapper productPriceMapper;

    @Override
    public IPage<ProductPrice> selectPage(Integer pageNo, Integer pageSize, Map paramMap) {
        int start = (pageNo - 1) * pageSize;
        paramMap.put("start",start);
        paramMap.put("size",pageSize);
        List<ProductPrice> list = productPriceMapper.selectPage(paramMap);
        long totalCount = productPriceMapper.totalCount(paramMap);
        Page page = new Page(pageNo, pageSize);
        page.setRecords(list);
        page.setTotal(totalCount);
        return page;
    }
}
