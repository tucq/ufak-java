package com.ufak.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ufak.order.entity.OrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 订单明细
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    /**
     * 根据订单id查找明细
     * @param orderId
     * @return
     */
    List<OrderDetail> queryByOrderId(@Param("orderId") String orderId);

}
