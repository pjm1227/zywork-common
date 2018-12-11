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

    // 小程序id
    public static final String APP_ID = "wx1fe3af6f9304f32b";

    // 小程序key或secret
    public static final String APP_KEY = "154c338a151d3c2f5f4fbdbb698acea9";

    // 通过获取的code获取openid和session_key
    public static final String AUTH_ACCESS_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APP_ID + "&secret=" + APP_KEY + "&js_code={JSCODE}&grant_type=authorization_code";

}