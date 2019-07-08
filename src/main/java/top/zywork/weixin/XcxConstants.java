package top.zywork.weixin;

/**
 * 微信小程序相关的通用常量<br/>
 *
 * 创建于2018-12-04<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class XcxConstants {

    /**
     * 通过获取的code获取openid和session_key
     */
    public static final String AUTH_ACCESS_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={APP_ID}&secret={APP_KEY}&js_code={JSCODE}&grant_type=authorization_code";

    /**
     * 获取用户凭证接口地址
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APP_ID}&secret={APP_KEY}";

    /**
     * 小程序发送模板消息
     */
    public static final String SEND_TEMPLATE_MSG = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token={ACCESS_TOKEN}";

    /**
     * 小程序端发送统一消息
     */
    public static final String SEND_UNIFORM_MSG = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send?access_token={ACCESS_TOKEN}";

}