package com.ufak.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.product.entity.ProductPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 商品价格库存
 * @Author: jeecg-boot
 * @Date:   2020-03-08
 * @Version: V1.0
 */
public interface ProductPriceMapper extends BaseMapper<ProductPrice> {

    List<ProductPrice> selectPage(Map paramMap);

    long totalCount(Map paramMap);

    void updateLevelOneSort(@Param("productId")String productId,@Param("specs1Id")String specs1Id,@Param("sort") int sort);

    void updateLevelTwoSort(@Param("productId")String productId,@Param("specs1Id")String specs1Id,@Param("specs2Id")String specs2Id,@Param("sort") int sort);
}
