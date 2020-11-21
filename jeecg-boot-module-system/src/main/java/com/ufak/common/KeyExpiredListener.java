package com.ufak.common;

import com.ufak.order.entity.Order;
import com.ufak.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Slf4j
public class KeyExpiredListener extends KeyExpirationEventMessageListener {
    @Autowired
    private IOrderService orderService;

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();//过期的key
        if(key.contains(Constants.ORDER_KEY_PREFIX)){
            String orderId = key.split("_")[1];
            Order order = orderService.getById(orderId);
            if(Constants.WAIT_PAY.equals(order.getOrderStatus())){ // 待付款状态
                try {
                    orderService.cancelOrder(orderId);//过期订单处理
                } catch (Exception e) {
                    log.info("redis处理过期订单:{}",e);
                }
            }
            log.info("过期订单key="+key);
        }
    }
}