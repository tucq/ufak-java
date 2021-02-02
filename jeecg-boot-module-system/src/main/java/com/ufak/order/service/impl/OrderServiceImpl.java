package com.ufak.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.ads.entity.FlashSale;
import com.ufak.ads.service.IFlashSaleService;
import com.ufak.common.Constants;
import com.ufak.order.entity.Order;
import com.ufak.order.entity.OrderDetail;
import com.ufak.order.mapper.OrderMapper;
import com.ufak.order.service.IOrderDetailService;
import com.ufak.order.service.IOrderService;
import com.ufak.order.service.IOrderUnpaidService;
import com.ufak.product.entity.ProductInfo;
import com.ufak.product.entity.ProductPrice;
import com.ufak.product.service.IProductInfoService;
import com.ufak.product.service.IProductPriceService;
import com.ufak.usr.entity.ShoppingCar;
import com.ufak.usr.service.IShoppingCarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private IShoppingCarService shoppingCarService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IProductPriceService productPriceService;
    @Autowired
    private IOrderUnpaidService orderUnpaidService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IProductInfoService productInfoService;
    @Autowired
    private IFlashSaleService flashSaleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitOrder(Order order, String payInfo) throws Exception {
        order.setUpdateTime(new Date());
        this.save(order);
        String orderId = order.getId();
        if(StringUtils.isNotBlank(payInfo) && !"null".equals(payInfo)){
            //商品立即购买
            JSONObject json = JSONObject.parseObject(payInfo);
            String productId = json.getString("productId");
            String specs1Id = json.getString("specs1Id");
            String specs2Id = json.getString("specs2Id");
            String buyNum = json.getString("buyNum");
            String flashSaleId = json.getString("flashSaleId");
            ProductPrice price = productPriceService.getPrice(productId,specs1Id,specs2Id);
            OrderDetail orderDetail = this.setOrderDetail(orderId,productId,specs1Id,specs2Id,buyNum,price.getPrice());
            orderDetailService.save(orderDetail);

            if(StringUtils.isEmpty(flashSaleId)){
                price.setStock(price.getStock() - Integer.valueOf(buyNum));// 更新库存数量
                boolean b = productPriceService.updateById(price);// 采用乐观锁更新库存
                if(!b){
                    throw new JeecgBootException("系统繁忙，请稍候重试！");
                }
            }else{
                // 更新秒杀库存
                FlashSale flashSale = flashSaleService.getById(flashSaleId);
                flashSale.setStock(flashSale.getStock() - 1);
                flashSaleService.updateById(flashSale);
            }

        }else {
            //商品通过购物车购买
            String userId = order.getUserId();
            List<ShoppingCar> list = shoppingCarService.getPayforList(userId);
            for(ShoppingCar car : list) {
                ProductPrice price = productPriceService.getPrice(car.getProductId(),car.getSpecs1Id(),car.getSpecs2Id());
                OrderDetail orderDetail = this.setOrderDetail(orderId,car.getProductId(),car.getSpecs1Id(),
                        car.getSpecs2Id(),String.valueOf(car.getBuyNum()),price.getPrice());
                orderDetailService.save(orderDetail);
                shoppingCarService.removeById(car.getId());

                price.setStock(price.getStock() - Integer.valueOf(car.getBuyNum()));// 更新库存数量
                boolean b = productPriceService.updateById(price);// 采用乐观锁更新库存
                if(!b){
                    throw new JeecgBootException("系统繁忙，请稍候重试！");
                }
            }

        }

        System.out.println("System.currentTimeMillis()="+System.currentTimeMillis()+" , order.getCreateTime()="+order.getCreateTime().getTime());

        long expireTime = (order.getCreateTime().getTime() + 30 * 60 * 1000 - System.currentTimeMillis()) / 1000;
        log.info("订单提交失效时间为："+expireTime+" 秒");
        redisUtil.set(Constants.ORDER_KEY_PREFIX + orderId,orderId,expireTime);//Redis 设置失效时间
    }

    private OrderDetail setOrderDetail(String orderId,String productId,String specs1Id,String specs2Id,String buyNum,BigDecimal price){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setProductId(productId);
        orderDetail.setSpecs1Id(specs1Id);
        if(StringUtils.isNotBlank(specs2Id) && !"null".equals(specs2Id)){
            orderDetail.setSpecs2Id(specs2Id);
        }
        orderDetail.setBuyNum(Integer.valueOf(buyNum));
        orderDetail.setPrice(price);
        return orderDetail;
    }


    @Override
    public IPage<Order> queryPageList(Integer pageNo, Integer pageSize, Map paramMap) {
        int start = (pageNo - 1) * pageSize;
        paramMap.put("start",start);
        paramMap.put("size",pageSize);
        List<Order> list = orderMapper.queryBackPageList(paramMap);
        long totalCount = orderMapper.totalBackCount(paramMap);
        Page page = new Page(pageNo, pageSize);
        page.setRecords(list);
        page.setTotal(totalCount);
        return page;
    }

    @Override
    public IPage<Order> queryAppPageList(Integer pageNo, Integer pageSize, Map paramMap) {
        int start = (pageNo - 1) * pageSize;
        paramMap.put("start",start);
        paramMap.put("size",pageSize);
        List<Order> list = orderMapper.queryAppPageList(paramMap);
        for(Order order : list){
            List orderDetails = orderDetailService.queryByOrderId(order.getId());
            order.setOrderDetails(orderDetails);
            order.setCreateTimeLong(order.getCreateTime().getTime());
        }
        long totalCount = orderMapper.totalCount(paramMap);
        Page page = new Page(pageNo, pageSize);
        page.setRecords(list);
        page.setTotal(totalCount);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderId) throws Exception{
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(Constants.CANCELLED);
        this.updateById(order);

        QueryWrapper<OrderDetail> qry = new QueryWrapper<>();
        qry.eq("order_id",orderId);
        List<OrderDetail> orderDetails = orderDetailService.list(qry);
        for(OrderDetail detail : orderDetails) {
            productPriceService.returnStock(detail.getProductId(),detail.getSpecs1Id(),detail.getSpecs2Id(),detail.getBuyNum());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(String orderId) throws Exception {
        this.removeById(orderId);
        QueryWrapper<OrderDetail> qryDetail = new QueryWrapper<>();
        qryDetail.eq("order_id",orderId);
        orderDetailService.remove(qryDetail);
        QueryWrapper qryUnpaid = new QueryWrapper<>();
        qryUnpaid.eq("order_id",orderId);
        orderUnpaidService.remove(qryUnpaid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buyAgain(String orderId) throws Exception {
        Order order = this.getById(orderId);
        List<OrderDetail> details = orderDetailService.queryByOrderId(orderId);
        UpdateWrapper uw = new UpdateWrapper();
        uw.set("is_check",Constants.NO);
        uw.eq("user_id",order.getUserId());
        shoppingCarService.update(uw);//取消其他购物车勾选项
        for(OrderDetail detail : details){
            //校验商品是否失效
            ProductInfo productInfo = productInfoService.getById(detail.getProductId());
            if(!"0".equals(productInfo.getState())){//商品已下架
                throw new JeecgBootException("非常抱歉该商品已下架，请您凉解");
            }
            //检查购物车商品是否已存在
            ShoppingCar sc = shoppingCarService.queryOne(order.getUserId(),detail.getProductId(),detail.getSpecs1Id(),detail.getSpecs2Id());
            if(sc != null){
                sc.setBuyNum(detail.getBuyNum());
                sc.setIsCheck(Constants.YES);
                shoppingCarService.updateById(sc);
            }else{
                ShoppingCar car = new ShoppingCar();
                car.setUserId(order.getUserId());
                car.setProductId(detail.getProductId());
                car.setSpecs1Id(detail.getSpecs1Id());
                car.setSpecs2Id(detail.getSpecs2Id());
                car.setBuyNum(detail.getBuyNum());
                car.setIsCheck(Constants.YES);
                car.setCreateTime(new Date());
                shoppingCarService.save(car);
            }
        }

    }
}
