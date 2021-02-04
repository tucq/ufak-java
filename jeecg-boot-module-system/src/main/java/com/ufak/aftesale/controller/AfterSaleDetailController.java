package com.ufak.aftesale.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ufak.aftesale.entity.AfterSale;
import com.ufak.aftesale.entity.AfterSaleDetail;
import com.ufak.aftesale.service.IAfterSaleDetailService;
import com.ufak.aftesale.service.IAfterSaleService;
import com.ufak.common.Constants;
import com.ufak.order.entity.Order;
import com.ufak.order.service.IOrderService;
import com.ufak.product.entity.ProductInfo;
import com.ufak.product.service.IProductInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 退款/售后明细
 * @Author: jeecg-boot
 * @Date: 2020-11-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "退款/售后明细")
@RestController
@RequestMapping("/afterSaleDetail")
public class AfterSaleDetailController extends JeecgController<AfterSaleDetail, IAfterSaleDetailService> {
    @Autowired
    private IAfterSaleService afterSaleService;
    @Autowired
    private IAfterSaleDetailService afterSaleDetailService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IProductInfoService productInfoService;


    /**
     * 校验所购买商品是否已过期
     *
     * @param orderId
     * @param productId
     * @return
     */
    @RequestMapping(value = "/check/expire")
    public Result<?> checkExpire(@RequestParam String orderId, @RequestParam String productId) {
        Order order = orderService.getById(orderId);
        Map<String, String> resultMap = new HashMap<>();
        if (order.getLogisticsTime() == null) {
            // TODO 特殊情况，物流未到货或丢失，或送货上门未确认
            resultMap.put("return", "0");
            resultMap.put("exchange", "0");
            resultMap.put("repair", "0");
        } else {
            Calendar logistics = DateUtils.getCalendar(order.getLogisticsTime().getTime());
            Calendar current = DateUtils.getCalendar();
            int subDay = DateUtils.dateDiff('d', current, logistics);//当前时间 - 到货时间
            ProductInfo product = productInfoService.getById(productId);
            List<Integer> valList = new ArrayList<>();
            valList.add(product.getRefundValidity());//退货
            valList.add(product.getBarterValidity());//换货
            valList.add(product.getRepairValidity());//维修
            String[] arg = new String[]{"return", "exchange", "repair"};//退货，换货，维修
            for (int i = 0; i < 3; i++) {
                if (valList.get(i) == 0) {
                    resultMap.put(arg[i], "-1");// 不支该服务
                } else {
                    if (subDay > valList.get(i)) {
                        resultMap.put(arg[i], "1");// 已过期
                    } else {
                        resultMap.put(arg[i], "0");// 未过期
                    }
                }
            }
        }
        return Result.ok(resultMap);
    }


    /**
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/apply")
    public Result<?> apply(@RequestBody JSONObject jsonObject) {
        try {
            String orderDetailId = jsonObject.getString("orderDetailId");
            String serviceType = jsonObject.getString("serviceType");
            QueryWrapper<AfterSale> qw = new QueryWrapper();
            qw.eq("order_detail_id",orderDetailId);
            qw.in("service_type",Constants.AFTER_SALE_RETURN,Constants.AFTER_SALE_EXCHANGE,Constants.AFTER_SALE_REPAIR);
            qw.eq("status",Constants.STATUS_PROCESS);
            List<AfterSale> list = afterSaleService.list(qw);
            if(list.size() > 0){
                AfterSale as = list.get(0);
                if(Constants.STATUS_PROCESS.equals(as.getStatus())){
                    return Result.error("该笔订单已申请售后，福安康将在24小时内会为您处理");
                }
            }
            afterSaleDetailService.apply(jsonObject);
            return Result.ok("申请售后成功");
        } catch (JeecgBootException e) {
            log.error("申请售后异常：{}", e);
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("申请售后异常：{}", e);
            return Result.error("申请售后异常,请联系客服");
        }
    }

    /**
     * 查看详情
     * @param id
     * @return
     */
    @GetMapping(value = "/view")
    public Result<?> view(@RequestParam(name="id",required=true) String id) {
        Map<String,Object> resultMap = new HashMap<>();
        AfterSale afterSale = afterSaleService.getById(id);
        QueryWrapper<AfterSaleDetail> qw = new QueryWrapper<>();
        qw.eq("after_sale_id",id);
        AfterSaleDetail afterSaleDetail = afterSaleDetailService.getOne(qw);
        resultMap.put("afterSale",afterSale);
        resultMap.put("afterSaleDetail",afterSaleDetail);
        return Result.ok(resultMap);
    }


    /**
     * 分页列表查询
     *
     * @param afterSaleDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(AfterSaleDetail afterSaleDetail,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<AfterSaleDetail> queryWrapper = QueryGenerator.initQueryWrapper(afterSaleDetail, req.getParameterMap());
        Page<AfterSaleDetail> page = new Page<AfterSaleDetail>(pageNo, pageSize);
        IPage<AfterSaleDetail> pageList = afterSaleDetailService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param afterSaleDetail
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody AfterSaleDetail afterSaleDetail) {
        afterSaleDetailService.save(afterSaleDetail);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param afterSaleDetail
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody AfterSaleDetail afterSaleDetail) {
        afterSaleDetailService.updateById(afterSaleDetail);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        afterSaleDetailService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.afterSaleDetailService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        AfterSaleDetail afterSaleDetail = afterSaleDetailService.getById(id);
        return Result.ok(afterSaleDetail);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param afterSaleDetail
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AfterSaleDetail afterSaleDetail) {
        return super.exportXls(request, afterSaleDetail, AfterSaleDetail.class, "退款/售后明细");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AfterSaleDetail.class);
    }

}
