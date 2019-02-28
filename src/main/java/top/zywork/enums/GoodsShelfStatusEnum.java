package top.zywork.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品上架状态枚举<br/>
 * 创建于2019-02-27<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum GoodsShelfStatusEnum {

    ON(0, "上架"),
    OFF(1, "下架");

    private Integer value;
    private String des;

    public static Map<Integer, GoodsShelfStatusEnum> lookup;

    static {
        lookup = new HashMap<>();
        for (GoodsShelfStatusEnum goodsCouponStatusEnum : EnumSet.allOf(GoodsShelfStatusEnum.class)) {
            lookup.put(goodsCouponStatusEnum.getValue(), goodsCouponStatusEnum);
        }
    }

    GoodsShelfStatusEnum(Integer value, String des) {
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

    public static GoodsShelfStatusEnum findByValue(Integer value) {
        return lookup.get(value);
    }

}
