import com.ufak.order.entity.Order;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2020/11/4.
 */
public class Test {
    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(100);
        BigDecimal b = new BigDecimal(100);
        String totalFee = String.valueOf("1001");
        Order order = new Order();
        order.setTotalFee(1001);
        System.out.println(order.getTotalFee() == Integer.valueOf(totalFee));
        System.out.println(order.getTotalFee() != Integer.valueOf(totalFee));
        System.out.println(order.getTotalFee().equals(Integer.valueOf(totalFee)));

    }
}
