package com.ufak.usr.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.usr.entity.ShoppingCar;
import com.ufak.usr.mapper.ShoppingCarMapper;
import com.ufak.usr.service.IShoppingCarService;
import org.springframework.stereotype.Service;

/**
 * @Description: 购物车
 * @Author: jeecg-boot
 * @Date:   2020-03-30
 * @Version: V1.0
 */
@Service
public class ShoppingCarServiceImpl extends ServiceImpl<ShoppingCarMapper, ShoppingCar> implements IShoppingCarService {

}
