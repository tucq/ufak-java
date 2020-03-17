package com.ufak.api;

import com.ufak.product.service.IProductInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags="小程序首页API")
@RestController
@RequestMapping("/api/home")
public class HomePageController {
    @Autowired
    private IProductInfoService productInfoService;

    @GetMapping(value = "/list")
    public Result<?> queryPageList() {
        return null;
    }
}
