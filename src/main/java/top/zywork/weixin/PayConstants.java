package top.zywork.weixin;

/**
 * 微信支付相关的通用常量<br/>
 *
 * 创建于2018-12-04<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class PayConstants {

    // 加密方式
    public static final String MD5 = "MD5";

    // 使用jsapi发起支付
    public static final String TRADE_JSAPI = "JSAPI";

    // 使用本地支付接口
    public static final String TRADE_NATIVE = "NATIVE";

    // 统一下单api地址
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    // 微信公众号发送普通红包api地址
    public static final String SEND_RED_PACK = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

    // 响应微信支付结果的内容
    public static final String PAY_NOTIFY_RESULT = "<xml>" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>" +
            "  <return_msg><![CDATA[OK]]></return_msg>" +
            "</xml>";

    // 微信支付返回码
    public static final String RETURN_CODE = "return_code";

    // 微信支付成功返回码
    public static final String RETURN_SUCCESS = "SUCCESS";

    // 微信支付失败返回码
    public static final String RETURN_FAIL = "FAIL";

    // 微信支付业务码
    public static final String RESULT_CODE = "result_code";

    // 微信支付错误码
    public static final String ERR_CODE = "err_code";

    // 微信支付错误码描述
    public static final String ERR_CODE_DES = "err_code_des";

}
