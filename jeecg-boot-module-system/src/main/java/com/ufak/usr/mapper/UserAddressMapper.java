package com.ufak.usr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.usr.entity.UserAddress;
import org.jeecg.modules.system.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @Description: 客户地址信息表
 * @Author: jeecg-boot
 * @Date:   2020-03-29
 * @Version: V1.0
 */
public interface UserAddressMapper extends BaseMapper<UserAddress> {

    List<SysUser> selectPage2(Map paramMap);

    long totalCount2(Map paramMap);

}
