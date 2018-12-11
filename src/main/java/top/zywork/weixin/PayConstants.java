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

    // 商户id
    public static final String MCH_ID = "1510233721";

    // 商户密钥
    public static final String API_KEY = "uktftujnabx9vba6glx5qq28dcodej28";

    // 加密方式
    public static final String MD5 = "MD5";

    // 使用jsapi发起支付
    public static final String TRADE_JSAPI = "JSAPI";

    // 使用本地支付接口
    public static final String TRADE_NATIVE = "NATIVE";

    // 统一下单api地址
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    // 支付回调地址，用于获取支付结果
    public static final String PAY_NOTIFY_URL = WeixinConstants.BASE_URL + "/byjc/tickeorder/result";

    // 响应微信支付结果的内容
    public static final String PAY_NOTIFY_RESULT = "<xml>" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>" +
            "  <return_msg><![CDATA[OK]]></return_msg>" +
            "</xml>";

}
