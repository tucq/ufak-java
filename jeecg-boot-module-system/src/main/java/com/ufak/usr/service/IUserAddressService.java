package com.ufak.usr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ufak.usr.entity.UserAddress;
import org.jeecg.modules.system.entity.SysUser;

import java.util.Map;

/**
 * @Description: 客户地址信息表
 * @Author: jeecg-boot
 * @Date:   2020-03-29
 * @Version: V1.0
 */
public interface IUserAddressService extends IService<UserAddress> {

    IPage<SysUser> selectPage2(Integer pageNo, Integer pageSize, Map paramMap);
}
