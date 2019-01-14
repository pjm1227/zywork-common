package top.zywork.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单状态的枚举<br/>
 * 创建于2019-01-14<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum GoodsOrderStatusEnum {

    WAIT_PAYMENT(0, "待付款"),
    PAY_SUCCESS(1, "已付款"),
    PAY_FAILURE(2, "支付失败"),
    WAIT_DELIVER(3, "待发货"),
    WAIT_TAKE_DELIVER(4, "待收货"),
    CONFIRMED(5, "已确认收货"),
    CANCELED(6, "已取消"),
    REPLY_RETURN(7, "已申请退货"),
    REJECT_RETURN(8, "拒绝退货"),
    RETURNING(9, "退货中"),
    RETURNED(10, "已退货");

    private Integer value;
    private String des;

    public static Map<Integer, GoodsOrderStatusEnum> lookup;

    static {
        lookup = new HashMap<>();
        for (GoodsOrderStatusEnum goodsOrderStatusEnum : EnumSet.allOf(GoodsOrderStatusEnum.class)) {
            lookup.put(goodsOrderStatusEnum.getValue(), goodsOrderStatusEnum);
        }
    }

    GoodsOrderStatusEnum(Integer value, String des) {
        this.value = value;
        this.des = des;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public static GoodsOrderStatusEnum findByValue(Integer value) {
        return lookup.get(value);
    }

}
