package top.zywork.weixin;

/**
 * 微信发送红包场景枚举值<br/>
 *
 * 创建于2019-01-16<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum RedpackSceneEnum {

    PRODUCT_1("PRODUCT_1", "商品促销"),
    PRODUCT_2("PRODUCT_2", "抽奖"),
    PRODUCT_3("PRODUCT_3", "虚拟物品兑奖"),
    PRODUCT_4("PRODUCT_4", "企业内部福利"),
    PRODUCT_5("PRODUCT_5", "渠道分润"),
    PRODUCT_6("PRODUCT_6", "保险回馈"),
    PRODUCT_7("PRODUCT_7", "彩票派奖"),
    PRODUCT_8("PRODUCT_8", "税务刮奖");

    private String value;
    private String des;

    RedpackSceneEnum(String value, String des) {
        this.value = value;
        this.des = des;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
