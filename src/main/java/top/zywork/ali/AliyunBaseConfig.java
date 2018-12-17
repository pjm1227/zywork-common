package top.zywork.ali;

/**
 * 阿里云SDK配置基础类<br/>
 * 创建于2018-12-15<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class AliyunBaseConfig {

    // 访问id，必填
    private String accessKeyId;
    // 访问密钥，必填
    private String accessKeySecret;

    public AliyunBaseConfig() {}

    public AliyunBaseConfig(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
