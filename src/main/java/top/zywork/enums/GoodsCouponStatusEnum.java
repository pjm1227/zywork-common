package top.zywork.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品优惠券状态枚举<br/>
 * 创建于2019-01-14<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum GoodsCouponStatusEnum {

    NOT_USE(0, "未使用"),
    USED(1, "已使用"),
    PAST_DUE(2, "已过期");

    private Integer value;
    private String des;

    public static Map<Integer, GoodsCouponStatusEnum> lookup;

    static {
        lookup = new HashMap<>();
        for (GoodsCouponStatusEnum goodsCouponStatusEnum : EnumSet.allOf(GoodsCouponStatusEnum.class)) {
            lookup.put(goodsCouponStatusEnum.getValue(), goodsCouponStatusEnum);
        }
    }

    GoodsCouponStatusEnum(Integer value, String des) {
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

    public static GoodsCouponStatusEnum findByValue(Integer value) {
        return lookup.get(value);
    }

}
