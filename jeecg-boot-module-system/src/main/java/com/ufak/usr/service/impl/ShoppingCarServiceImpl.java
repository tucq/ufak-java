package com.ufak.usr.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.usr.entity.ShoppingCar;
import com.ufak.usr.mapper.ShoppingCarMapper;
import com.ufak.usr.service.IShoppingCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date:   2020-03-30
 * @Version: V1.0
 */
@Service
public class ShoppingCarServiceImpl extends ServiceImpl<ShoppingCarMapper, ShoppingCar> implements IShoppingCarService {
    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @Override
    public IPage<ShoppingCar> selectPage(Integer pageNo, Integer pageSize, Map paramMap) {
        int start = (pageNo - 1) * pageSize;
        paramMap.put("start",start);
        paramMap.put("size",pageSize);
        List<ShoppingCar> list = shoppingCarMapper.selectPage(paramMap);
        long totalCount = shoppingCarMapper.totalCount(paramMap);
        Page page = new Page(pageNo, pageSize);
        page.setRecords(list);
        page.setTotal(totalCount);
        return page;
    }

    @Override
    public List<ShoppingCar> getPayforList(String userId) {
        QueryWrapper<ShoppingCar> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);
        qw.eq("is_check","0");
        return this.list(qw);
    }
}
