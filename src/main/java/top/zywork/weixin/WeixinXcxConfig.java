package top.zywork.weixin;

/**
 * 微信小程序相关配置类<br/>
 * 创建于2018-12-17<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class WeixinXcxConfig extends WeixinBaseConfig {

    // 公众号id
    private String appId;
    // 公众号key
    private String appSecret;

    public WeixinXcxConfig() {}

    public WeixinXcxConfig(String appId, String appSecret, String baseUrl) {
        super(baseUrl);
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

}
