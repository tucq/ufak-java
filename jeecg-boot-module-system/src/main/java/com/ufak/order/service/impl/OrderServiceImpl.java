package com.ufak.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ufak.order.entity.Order;
import com.ufak.order.entity.OrderDetail;
import com.ufak.order.mapper.OrderMapper;
import com.ufak.order.service.IOrderDetailService;
import com.ufak.order.service.IOrderService;
import com.ufak.product.entity.ProductPrice;
import com.ufak.product.service.IProductPriceService;
import com.ufak.usr.entity.ShoppingCar;
import com.ufak.usr.service.IShoppingCarService;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 订单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private IShoppingCarService shoppingCarService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IProductPriceService productPriceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitOrder(Order order, String payInfo) throws Exception {
        this.save(order);
        String orderId = order.getId();
        if(StringUtils.isNotBlank(payInfo) && !"null".equals(payInfo)){
            //商品立即购买
            JSONObject json = JSONObject.parseObject(payInfo);
            String productId = json.getString("productId");
            String specs1Id = json.getString("specs1Id");
            String specs2Id = json.getString("specs2Id");
            String buyNum = json.getString("buyNum");
            ProductPrice price = productPriceService.getPrice(productId,specs1Id,specs2Id);
            OrderDetail orderDetail = this.setOrderDetail(orderId,productId,specs1Id,specs2Id,buyNum,price.getPrice());
            orderDetailService.save(orderDetail);

            price.setStock(price.getStock() - Integer.valueOf(buyNum));// 更新库存数量
            boolean b = productPriceService.updateById(price);// 采用乐观锁更新库存
            if(!b){
                throw new JeecgBootException("系统繁忙，请稍候重试！");
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

}
