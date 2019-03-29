package top.zywork.enums;

/**
 * 存储提供者枚举<br/>
 * 创建于2019-03-29<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum StorageProviderEnum {

    LOCAL("local", "本地存储"),
    ALIYUN_OSS("aliyun-oss", "阿里云OSS"),
    QINIU_OSS("qiniu-oss", "七牛云对象存储");

    private String provider;
    private String des;

    StorageProviderEnum(String provider, String des) {
        this.provider = provider;
        this.des = des;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
