package top.zywork.enums;

/**
 * 哈希算法编码方式枚举<br/>
 *
 * 创建于2018-05-21<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum HashEncodeEnum {

    HEX("HEX"),
    BASE64("BASE64");

    private String value;

    HashEncodeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
