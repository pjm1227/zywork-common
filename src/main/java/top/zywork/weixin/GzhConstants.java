package top.zywork.weixin;

/**
 * 微信公众号相关的通用常量<br/>
 *
 * 创建于2018-12-04<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class GzhConstants {

    /**
     * 公众号授权登录，以获取code
     */
    public static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={APP_ID}&redirect_uri={REDIRECT_URL}" + "&response_type=code&scope=snsapi_userinfo&state=access#wechat_redirect";

    /**
     * 通过获取的code获取access_token和openid
     */
    public static final String AUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={APP_ID}&secret={APP_KEY}&code={CODE}&grant_type=authorization_code";

    /**
     * 获取用户凭证接口地址
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APP_ID}&secret={APP_KEY}";

    /**
     * 通过获取的access_token和openid获取用户信息，此种方式获取不到unionid
     */
    public static final String SNS_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token={ACCESS_TOKEN}&openid={OPENID}&lang=zh_CN";

    /**
     * 通过获取的access_token和openid获取用户信息，此种方式可获取unionid，但前提是用户关注了公众号
     */
    public static final String USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={ACCESS_TOKEN}&openid={OPENID}&lang=zh_CN";

    /**
     * 公众号发送模板消息
     */
    public static final String SEND_TEMPLATE_MSG = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={ACCESS_TOKEN}";

}