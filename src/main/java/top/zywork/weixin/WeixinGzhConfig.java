package top.zywork.weixin;

/**
 * 微信公众号相关配置类<br/>
 * 创建于2018-12-17<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class WeixinGzhConfig extends WeixinBaseConfig {

    // 公众号id
    private String appId;
    // 公众号key
    private String appSecret;
    // 公众号登录回调地址
    private String loginRedirectUrl;

    public WeixinGzhConfig() {}

    public WeixinGzhConfig(String appId, String appSecret, String baseUrl, String loginRedirectUrl) {
        super(baseUrl);
        this.appId = appId;
        this.appSecret = appSecret;
        this.loginRedirectUrl = loginRedirectUrl;
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

    public String getLoginRedirectUrl() {
        return loginRedirectUrl;
    }

    public void setLoginRedirectUrl(String loginRedirectUrl) {
        this.loginRedirectUrl = loginRedirectUrl;
    }

}
