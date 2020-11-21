package com.ufak.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.product.entity.ProductPrice;
import com.ufak.product.mapper.ProductPriceMapper;
import com.ufak.product.service.IProductPriceService;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
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

    @Override
    public List<ProductPrice> queryProductPrice(String productId) {
        return productPriceMapper.queryProductPrice(productId);
    }

    @Override
    public ProductPrice getPrice(String productId, String specs1Id, String specs2Id) {
        QueryWrapper<ProductPrice> qw = new QueryWrapper<>();
        qw.eq("product_id",productId);
        qw.eq("specs1_id",specs1Id);
        if(StringUtils.isNotBlank(specs2Id) && !"null".equals(specs2Id)){
            qw.eq("specs2_id",specs2Id);
        }
        return this.getOne(qw);
    }

    @Override
    public void returnStock(String productId, String specs1Id, String specs2Id, Integer buyNum) throws Exception {
        ProductPrice price = this.getPrice(productId,specs1Id,specs2Id);
        price.setStock(price.getStock() + buyNum);// 订单取消后库存加回去
        boolean b = this.updateById(price);// 采用乐观锁更新库存
        if(!b){
            throw new JeecgBootException("系统繁忙，请稍候重试！");
        }
    }
}
