package com.ufak.aftesale.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.mapper.AfterSaleMapper;
import com.ufak.aftesale.service.IAfterSaleService;
import com.ufak.order.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 退款/售后
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Service
public class AfterSaleServiceImpl extends ServiceImpl<AfterSaleMapper, AfterSale> implements IAfterSaleService {

    @Autowired
    private AfterSaleMapper afterSaleMapper;
    @Autowired
    private IOrderDetailService orderDetailService;

    @Override
    public IPage<AfterSale> queryPageList(Integer pageNo, Integer pageSize, Map paramMap) {
        int start = (pageNo - 1) * pageSize;
        paramMap.put("start",start);
        paramMap.put("size",pageSize);
        List<AfterSale> list = afterSaleMapper.queryPageList(paramMap);
        for(AfterSale afterSale : list){
            List orderDetails = orderDetailService.queryByOrderId(afterSale.getOrderId());
            afterSale.setOrderDetails(orderDetails);
        }
        long totalCount = afterSaleMapper.totalCount(paramMap);
        Page page = new Page(pageNo, pageSize);
        page.setRecords(list);
        page.setTotal(totalCount);
        return page;
    }
}
