package top.zywork.enums;

/**
 * SSL/TLS协议枚举<br/>
 * 创建于2019-06-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum SSLProtocolEnum {

    TLSv1("TLSv1"),
    TLSv1_1("TLSv1.1"),
    TLSv1_2("TLSv1.2");

    private String value;

    SSLProtocolEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
