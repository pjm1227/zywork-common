package top.zywork.qiniu;

/**
 * 七牛sdk配置基础类<br/>
 * 创建于2019-03-28<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class QiniuBaseConfig {

    /**
     * key
     */
    private String accessKey;
    /**
     * secret
     */
    private String secretKey;

    public QiniuBaseConfig() {}

    public QiniuBaseConfig(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
