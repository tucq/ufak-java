package com.ufak.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ufak.ads.entity.HomepageAds;
import com.ufak.ads.service.IHomepageAdsService;
import com.ufak.common.Constants;
import com.ufak.product.entity.ProductInfo;
import com.ufak.product.service.IProductInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags="小程序首页API")
@RestController
@RequestMapping("/api/home")
public class HomePageController {
    @Autowired
    private IProductInfoService productInfoService;
    @Autowired
    private IHomepageAdsService homepageAdsService;

    @GetMapping(value = "/list")
    public Result<Map> queryPageList() {
        Result<Map> result = new Result<>();
        QueryWrapper qwAds = new QueryWrapper();
        qwAds.eq("state", Constants.YES);
        qwAds.orderByAsc("sort");
        List<HomepageAds> adsList = homepageAdsService.list(qwAds);

        Map<String,Object> map = new HashMap<>();
        List<HomepageAds> headList = new ArrayList<>();
        List<HomepageAds> categoryList = new ArrayList<>();
        List<HomepageAds> insertList = new ArrayList<>();
        for(HomepageAds ads: adsList){
            if(Constants.ADS_TYPE_HEAD.equals(ads.getType())){
                headList.add(ads);
            }else if(Constants.ADS_TYPE_CATEGORY.equals(ads.getType())){
                categoryList.add(ads);
            }else if(Constants.ADS_TYPE_INSERT.equals(ads.getType())){
                insertList.add(ads);
            }
        }

        QueryWrapper productQw = new QueryWrapper();
        productQw.eq("state",Constants.YES);
        productQw.orderByDesc("sales_volume");
        List<ProductInfo> productList = productInfoService.list();

        map.put("headList",headList);
        map.put("categoryList",categoryList);
        map.put("insertList",insertList);
        map.put("productList",productList);
        result.setResult(map);
        result.setSuccess(true);
        return result;
    }
}
