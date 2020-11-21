import org.jeecg.common.util.DateUtils;

/**
 * Created by Administrator on 2020/11/4.
 */
public class Test {
    public static void main(String[] args) {
//        int x;//定义两变量
//        Random ne=new Random();//实例化一个random的对象ne
//        x=ne.nextInt(9999-1000+1)+1000;//为变量赋随机值1000-9999
//        System.out.println("产生的随机数是:"+x);//输出
//
//        System.out.println(Math.round((Math.random()+1) * 1000));

        String productNames = "aaa/bb/cc///";
        productNames = productNames.substring(0,productNames.lastIndexOf("/"));
        System.out.println(productNames);

        long curr = System.currentTimeMillis();
        long expr2 = curr + 30 * 60 * 1000;
        long expr1 = curr + 90 * 1000;
        System.out.println(expr1);
        System.out.println(expr2);
        System.out.println(DateUtils.formatDate(DateUtils.getCalendar(curr),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtils.formatDate(DateUtils.getCalendar(expr2),"yyyy-MM-dd HH:mm:ss"));
    }
}
