package top.zywork.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付方式枚举<br/>
 * 创建于2019-01-14<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum PaymentTypeEnum {

    WEIXIN_PAY(0, "微信支付"),
    ALIPAY(1, "支付宝支付"),
    UNION_PAY(2, "银联支付"),
    WALLET_PAY(3, "余额支付");

    private Integer value;
    private String des;

    public static Map<Integer, PaymentTypeEnum> lookup;

    static {
        lookup = new HashMap<>();
        for (PaymentTypeEnum paymentTypeEnum : EnumSet.allOf(PaymentTypeEnum.class)) {
            lookup.put(paymentTypeEnum.getValue(), paymentTypeEnum);
        }
    }

    PaymentTypeEnum(Integer value, String des) {
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

    public static PaymentTypeEnum findByValue(Integer value) {
        return lookup.get(value);
    }

}
