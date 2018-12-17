package top.zywork.weixin;

/**
 * 微信支付相关配置类<br/>
 * 创建于2018-12-17<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class WXPayConfig extends WeixinBaseConfig {

    // 商户id
    private String mchId;
    // 商户api secret
    private String apiSecret;
    // 支付通知地址
    private String payNotifyUrl;

    public WXPayConfig() {}

    public WXPayConfig(String mchId, String apiSecret, String baseUrl, String payNotifyUrl) {
        super(baseUrl);
        this.mchId = mchId;
        this.apiSecret = apiSecret;
        this.payNotifyUrl = payNotifyUrl;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getPayNotifyUrl() {
        return payNotifyUrl;
    }

    public void setPayNotifyUrl(String payNotifyUrl) {
        this.payNotifyUrl = payNotifyUrl;
    }
}
