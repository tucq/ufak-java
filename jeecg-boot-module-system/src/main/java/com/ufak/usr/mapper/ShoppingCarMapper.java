package com.ufak.usr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.usr.entity.ShoppingCar;

import java.util.List;
import java.util.Map;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date:   2020-03-30
 * @Version: V1.0
 */
public interface ShoppingCarMapper extends BaseMapper<ShoppingCar> {

    List<ShoppingCar> selectPage(Map paramMap);

    long totalCount(Map paramMap);
}
