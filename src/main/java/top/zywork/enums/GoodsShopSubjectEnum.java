package top.zywork.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 店铺主体类型枚举<br/>
 * 创建于2019-01-14<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum GoodsShopSubjectEnum {

    PERSONALE(0, "个人"),
    COMPANY(1, "公司");

    private Integer value;
    private String des;

    public static Map<Integer, GoodsShopSubjectEnum> lookup;

    static {
        lookup = new HashMap<>();
        for (GoodsShopSubjectEnum goodsShopSubjectEnum : EnumSet.allOf(GoodsShopSubjectEnum.class)) {
            lookup.put(goodsShopSubjectEnum.getValue(), goodsShopSubjectEnum);
        }
    }

    GoodsShopSubjectEnum(Integer value, String des) {
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

    public static GoodsShopSubjectEnum findByValue(Integer value) {
        return lookup.get(value);
    }

}
