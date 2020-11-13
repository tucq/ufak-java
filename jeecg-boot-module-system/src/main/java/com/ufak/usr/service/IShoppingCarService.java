package com.ufak.usr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.usr.entity.ShoppingCar;

import java.util.List;
import java.util.Map;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date:   2020-03-30
 * @Version: V1.0
 */
public interface IShoppingCarService extends IService<ShoppingCar> {

    IPage<ShoppingCar> selectPage(Integer pageNo, Integer pageSize, Map paramMap);

    /**
     * 获取支付的购物车数据
     * @param userId
     * @return
     */
    List<ShoppingCar> getPayforList(String userId);

}
