package top.zywork.weixin;

/**
 * 微信配置基础类<br/>
 * 创建于2018-12-17<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class WeixinBaseConfig {

    // base url
    private String baseUrl;

    public WeixinBaseConfig() {}

    public WeixinBaseConfig(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
