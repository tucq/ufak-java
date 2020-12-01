package com.ufak.aftesale.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.mapper.AfterSaleMapper;
import com.ufak.aftesale.service.IAfterSaleService;
import org.springframework.stereotype.Service;

/**
 * @Description: 退款/售后
 * @Author: jeecg-boot
 * @Date:   2020-11-28
 * @Version: V1.0
 */
@Service
public class AfterSaleServiceImpl extends ServiceImpl<AfterSaleMapper, AfterSale> implements IAfterSaleService {

}
