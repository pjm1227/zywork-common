package top.zywork.enums;

/**
 * 社交类型枚举<br/>
 * 创建于2018-12-06<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum SocialTypeEnum {

    WEIXIN("微信"),
    QQ("QQ"),
    ALIPAY("支付宝");

    private String value;

    SocialTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
