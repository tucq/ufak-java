package com.ufak.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ufak.ads.entity.FlashSale;
import com.ufak.ads.entity.HomepageAds;
import com.ufak.ads.service.IFlashSaleService;
import com.ufak.ads.service.IHomepageAdsService;
import com.ufak.common.Constants;
import com.ufak.product.entity.ProductInfo;
import com.ufak.product.service.IProductInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private IFlashSaleService flashSaleService;

    @GetMapping(value = "/list")
    public Result<Map> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
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

        Map paramMap = new HashMap<>();
        paramMap.put("salesVolume","desc");//按销量排序
        IPage<ProductInfo> pageList = productInfoService.queryPhoneProductPage(pageNo,pageSize,paramMap);

        paramMap.put("state", Constants.YES);//限时抢购
        IPage<FlashSale> flashSalePage = flashSaleService.selectPage(1,9999,paramMap);// 限时抢购
        for(FlashSale flashSale : flashSalePage.getRecords()){
            flashSale.setStartTime(DateUtils.formatDate() + " " + flashSale.getStartTime());
            flashSale.setEndTime(DateUtils.formatDate() + " " + flashSale.getEndTime());
        }

        pageList.getRecords();
        map.put("headList",headList);
        map.put("categoryList",categoryList);
        map.put("insertList",insertList);
        map.put("productList",pageList.getRecords());
        map.put("flashSaleList",flashSalePage.getRecords());
        result.setResult(map);
        result.setSuccess(true);
        return result;
    }


    /**
     * 首页随便看看商品
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/random/product")
    public Result<?> queryHomeProductPage(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
        Map paramMap = new HashMap<>();
        paramMap.put("salesVolume","desc");//按销量排序
        IPage<ProductInfo> pageList = productInfoService.queryPhoneProductPage(pageNo,pageSize,paramMap);
        return Result.ok(pageList);
    }


    /**
     * 首页轮播图,分类, 插拔广告关联商品查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/ads/product")
    public Result<?> queryAdsProduct(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
        String adsId = req.getParameter("adsId");
        Map paramMap = new HashMap<>();
        if(StringUtils.isNotBlank(adsId)){
            paramMap.put("adsId",adsId);
        }
        IPage<ProductInfo> pageList = productInfoService.queryAdsProductPage(pageNo,pageSize,paramMap);
        return Result.ok(pageList);
    }

    @GetMapping(value = "/notifyUrl")
    public Result<?> notifyUrl(HttpServletRequest req, HttpServletResponse res) {
        return Result.ok("支付成功");
    }


    @RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
    public Result<List<DictModel>> getDictItems(@PathVariable String dictCode) {
        log.info(" dictCode : "+ dictCode);
        Result<List<DictModel>> result = new Result<List<DictModel>>();
        List<DictModel> ls = null;
        try {
            if(dictCode.indexOf(",")!=-1) {
                //关联表字典（举例：sys_user,realname,id）
                String[] params = dictCode.split(",");

                if(params.length<3) {
                    result.error500("字典Code格式不正确！");
                    return result;
                }
                //SQL注入校验（只限制非法串改数据库）
                final String[] sqlInjCheck = {params[0],params[1],params[2]};
                SqlInjectionUtil.filterContent(sqlInjCheck);

                if(params.length==4) {
                    //SQL注入校验（查询条件SQL 特殊check，此方法仅供此处使用）
                    SqlInjectionUtil.specialFilterContent(params[3]);
                    ls = sysDictService.queryTableDictItemsByCodeAndFilter(params[0],params[1],params[2],params[3]);
                }else if (params.length==3) {
                    ls = sysDictService.queryTableDictItemsByCode(params[0],params[1],params[2]);
                }else{
                    result.error500("字典Code格式不正确！");
                    return result;
                }
            }else {
                //字典表
                ls = sysDictService.queryDictItemsByCode(dictCode);
            }

            result.setSuccess(true);
            result.setResult(ls);
            log.info(result.toString());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
            return result;
        }

        return result;
    }
}
