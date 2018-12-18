package top.zywork.enums;

/**
 * 移动端常用App枚举<br/>
 * 创建于2018-12-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum ClientAppEnum {

    WEIXIN("微信"),
    QQ("QQ"),
    OTHER("其他");

    private String value;

    ClientAppEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
