package top.zywork.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品优惠券使用范围枚举<br/>
 * 创建于2019-01-14<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum GoodsCouponUsableRangeEnum {

    PLATFORM_COUPON(0, "平台优惠券"),
    CATEGORY_COUPON(1, "类目优惠券"),
    SHOP_COUPON(2, "店铺优惠券"),
    GOODS_COUPON(3, "商品优惠券"),
    SKU_COUPON(4, "单品优惠券");

    private Integer value;
    private String des;

    public static Map<Integer, GoodsCouponUsableRangeEnum> lookup;

    static {
        lookup = new HashMap<>();
        for (GoodsCouponUsableRangeEnum goodsCouponUsableRangeEnum : EnumSet.allOf(GoodsCouponUsableRangeEnum.class)) {
            lookup.put(goodsCouponUsableRangeEnum.getValue(), goodsCouponUsableRangeEnum);
        }
    }

    GoodsCouponUsableRangeEnum(Integer value, String des) {
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

    public static GoodsCouponUsableRangeEnum findByValue(Integer value) {
        return lookup.get(value);
    }

}
