package com.ufak.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.common.Constants;
import com.ufak.product.entity.ProductInfo;
import com.ufak.product.entity.ProductPrice;
import com.ufak.product.entity.ProductSpecs;
import com.ufak.product.mapper.ProductInfoMapper;
import com.ufak.product.mapper.ProductPriceMapper;
import com.ufak.product.service.IProductInfoService;
import com.ufak.product.service.IProductPriceService;
import com.ufak.product.service.IProductSpecsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @Autowired
    private IProductPriceService productPriceService;
    @Autowired
    private ProductPriceMapper productPriceMapper;

    @Override
    public void saveProductInfo(ProductInfo productInfo) {
        this.save(productInfo);
        insertProductSpecs(productInfo);
        this.savePrice(productInfo.getId());
    }

    @Override
    public void updateProductInfo(ProductInfo productInfo) {
        this.updateById(productInfo);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("product_id",productInfo.getId());
        productSpecsService.remove(qw);
        insertProductSpecs(productInfo);

        List<ProductSpecs> removeList = productInfo.getRemoveProductSpecsList();
        for(ProductSpecs ps : removeList){
            if(StringUtils.isNotEmpty(ps.getId())){
                productSpecsService.removeById(ps.getId());//删除规格

                //删除价格库存
                QueryWrapper rw = new QueryWrapper();
                rw.eq("product_id",productInfo.getId());
                if(Constants.LEVEL_ONE.equals(ps.getLevel())){
                    rw.eq("specs1_id",ps.getId());
                }else if(Constants.LEVEL_TWO.equals(ps.getLevel())){
                    rw.eq("specs2_id",ps.getId());
                }
                productPriceService.remove(rw);
            }
        }

        this.savePrice(productInfo.getId());
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

    /**
     * 新增商品价格库存
     * @param productId
     */
    private void savePrice(String productId){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("product_id",productId);
        qw.ne("pid",'0');
        qw.eq("level",'0');
        qw.orderByAsc("sort");
        List<ProductSpecs> specsOne = productSpecsService.list(qw);

        QueryWrapper qw2 = new QueryWrapper();
        qw2.eq("product_id",productId);
        qw2.ne("pid",'0');
        qw2.eq("level",'1');
        qw2.orderByAsc("sort");
        List<ProductSpecs> specsTwo = productSpecsService.list(qw2);
        int sort = 0;
        if(specsTwo.size() > 0){
            for(ProductSpecs specs1 : specsOne){
                for(ProductSpecs specs2 : specsTwo){
                    sort ++;
                    saveLevelTwoPrice(specs1,specs2,sort);
                }
            }
        }else{
            for(ProductSpecs specs : specsOne){
                sort ++;
                saveLevelOnePrice(specs,sort);
            }
        }

    }

    private void saveLevelOnePrice(ProductSpecs specs,int sort){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("product_id",specs.getProductId());
        qw.eq("specs1_id",specs.getId());
        int ct = productPriceService.count(qw);
        if(ct == 0){
            ProductPrice price = getPriceObj(specs,0);
            price.setSort(sort);
            productPriceService.save(price);
        }else{
            productPriceMapper.updateLevelOneSort(specs.getProductId(),specs.getId(),sort);
        }
    }

    private void saveLevelTwoPrice(ProductSpecs specsOne,ProductSpecs specsTwo,int sort){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("product_id",specsOne.getProductId());
        qw.eq("specs1_id",specsOne.getId());
        qw.eq("specs2_id",specsTwo.getId());
        int ct = productPriceService.count(qw);
        if(ct == 0){
            ProductPrice price2 = getPriceObj(specsTwo,1);
            price2.setSpecs1Id(specsOne.getId());
            price2.setSort(sort);
            productPriceService.save(price2);
        }else{
            productPriceMapper.updateLevelTwoSort(specsOne.getProductId(),specsOne.getId(),specsTwo.getId(),sort);
        }
    }

    private ProductPrice getPriceObj(ProductSpecs specs,int level){
        ProductPrice price = new ProductPrice();
        price.setProductId(specs.getProductId());
        if(level == 0){
            price.setSpecs1Id(specs.getId());
        }else{
            price.setSpecs2Id(specs.getId());
        }
        price.setPrice(BigDecimal.ZERO);
        price.setVirtualPrice(BigDecimal.ZERO);
        price.setDiscount(new BigDecimal("1"));
        price.setStock(0);
        return  price;
    }
}
