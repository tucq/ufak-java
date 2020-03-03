package com.ufak.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.common.Constants;
import com.ufak.product.entity.ProductInfo;
import com.ufak.product.entity.ProductSpecs;
import com.ufak.product.mapper.ProductInfoMapper;
import com.ufak.product.service.IProductInfoService;
import com.ufak.product.service.IProductSpecsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        insertProductSpecs(productInfo);
    }

    private void insertProductSpecs(ProductInfo productInfo){
        String productId = productInfo.getId();
        List<ProductSpecs> list = productInfo.getProductSpecsList();
        String oneId = null;
        String twoId = null;
        int sort = 0;
        for(ProductSpecs ps : list){
            if("0".equals(ps.getPid())){
                sort ++;
                ps.setProductId(productId);
                ps.setSort(sort);
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
                sort ++;
                ps.setProductId(productId);
                ps.setSort(sort);
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

    @Override
    public void updateProductInfo(ProductInfo productInfo) {
        this.updateById(productInfo);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("product_id",productInfo.getId());
        productSpecsService.remove(qw);
        insertProductSpecs(productInfo);

//        List<ProductSpecs> list = productInfo.getProductSpecsList();
//        int sort = 0;
//        for(ProductSpecs ps : list){
//            sort ++;
//            ps.setSort(sort);
//            if(StringUtils.isNotEmpty(ps.getId())){
//                productSpecsService.updateById(ps);
//            }else{
//                ps.setProductId(productInfo.getId());
//                productSpecsService.save(ps);
//            }
//        }

        List<ProductSpecs> removeList = productInfo.getRemoveProductSpecsList();
        for(ProductSpecs ps : removeList){
            if(StringUtils.isNotEmpty(ps.getId())){
                productSpecsService.removeById(ps.getId());
            }
        }

    }
}
