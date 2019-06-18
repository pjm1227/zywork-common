package top.zywork.enums;

/**
 * 数字证书类型枚举<br/>
 * 创建于2019-06-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum CertTypeEnum {

    PKCS12("PKCS12"),
    BKS("BKS"),
    JKS("JKS"),
    JCEKS("JCEKS"),
    UBER("UBER");

    private String value;

    CertTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
