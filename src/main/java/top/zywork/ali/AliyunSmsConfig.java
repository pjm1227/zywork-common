package top.zywork.ali;

/**
 * 阿里云短信配置类<br/>
 * 创建于2018-12-15<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class AliyunSmsConfig extends AliyunBaseConfig {

    // 短信签名，必填
    private String signName;

    public AliyunSmsConfig() {}

    public AliyunSmsConfig(String accessKeyId, String accessKeySecret, String signName) {
        super(accessKeyId, accessKeySecret);
        this.signName = signName;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
}
