package top.zywork.enums;

/**
 * 常用操作系统枚举<br/>
 * 创建于2018-12-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum ClientOSEnum {

    WINDOWS("Windows"),
    MAC("Mac"),
    IOS("iOS"),
    ANDROID("Android"),
    LINUX("Linux"),
    OTHER("其他");

    private String value;

    ClientOSEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
