package com.ufak.usr.service.impl;

import com.ufak.usr.entity.UserProduct;
import com.ufak.usr.mapper.UserProductMapper;
import com.ufak.usr.service.IUserProductService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户收藏列表
 * @Author: jeecg-boot
 * @Date:   2020-12-04
 * @Version: V1.0
 */
@Service
public class UserProductServiceImpl extends ServiceImpl<UserProductMapper, UserProduct> implements IUserProductService {

}
