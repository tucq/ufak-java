package com.ufak.product.service.impl;

import com.ufak.common.Constants;
import com.ufak.product.entity.ProductInfo;
import com.ufak.product.entity.ProductSpecs;
import com.ufak.product.mapper.ProductInfoMapper;
import com.ufak.product.service.IProductInfoService;
import com.ufak.product.service.IProductSpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.UUID;

/**
 * @Description: 商品信息
 * @Author: jeecg-boot
 * @Date:   2020-02-24
 * @Version: V1.0
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements IProductInfoService {

    @Autowired
    private IProductSpecsService productSpecsService;

    @Override
    public void saveProductInfo(ProductInfo productInfo) {
        this.save(productInfo);
        String productId = productInfo.getId();
        List<ProductSpecs> list = productInfo.getProductSpecsList();
        String oneId = null;
        String twoId = null;
        for(ProductSpecs ps : list){
            if("0".equals(ps.getPid())){
                ps.setProductId(productId);
                productSpecsService.save(ps);
                if(Constants.LEVEL_ONE.equals(ps.getLevel())){
                    oneId = ps.getId();
                }
                if(Constants.LEVEL_TWO.equals(ps.getLevel())){
                    twoId = ps.getId();
                }
            }
        }

        for(ProductSpecs ps : list){
            if(!"0".equals(ps.getPid())){
                ps.setProductId(productId);
                if(Constants.LEVEL_ONE.equals(ps.getLevel())){
                    ps.setPid(oneId);
                }
                if(Constants.LEVEL_TWO.equals(ps.getLevel())){
                    ps.setPid(twoId);
                }

                productSpecsService.save(ps);
            }
        }
    }
}
