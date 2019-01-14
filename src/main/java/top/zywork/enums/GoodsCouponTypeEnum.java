package top.zywork.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品优惠券类型枚举<br/>
 * 创建于2019-01-14<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum GoodsCouponTypeEnum {

    CASH_COUPON(0, "现金券"),
    DISCOUNT_COUPON(1, "折扣券"),
    INTEGRAL_COUPON(2, "积分券");

    private Integer value;
    private String des;

    public static Map<Integer, GoodsCouponTypeEnum> lookup;

    static {
        lookup = new HashMap<>();
        for (GoodsCouponTypeEnum goodsCouponTypeEnum : EnumSet.allOf(GoodsCouponTypeEnum.class)) {
            lookup.put(goodsCouponTypeEnum.getValue(), goodsCouponTypeEnum);
        }
    }

    GoodsCouponTypeEnum(Integer value, String des) {
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

    public static GoodsCouponTypeEnum findByValue(Integer value) {
        return lookup.get(value);
    }

}
