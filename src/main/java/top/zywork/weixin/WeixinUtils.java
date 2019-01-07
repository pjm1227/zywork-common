package top.zywork.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.common.DateUtils;
import top.zywork.common.HttpUtils;
import top.zywork.common.IOUtils;
import top.zywork.common.UUIDUtils;
import top.zywork.enums.CharsetEnum;
import top.zywork.enums.ContentTypeEnum;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信登录和支付相关工具类<br/>
 *
 * 创建于2018-12-04<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class WeixinUtils {

    private static final Logger logger = LoggerFactory.getLogger(WeixinUtils.class);

    /**
     * 从哪个url进入到公众号授权界面
     * @param appId
     * @param loginRedirectUrl
     * @param fromUrl
     * @param shareCode
     * @return
     */
    public static String gzhAuthorizeUrl(String appId, String loginRedirectUrl, String fromUrl, String shareCode) {
        loginRedirectUrl += "?t=" + DateUtils.currentTimeMillis();
        loginRedirectUrl = loginRedirectUrl + (StringUtils.isEmpty(fromUrl) ? "" : "&fromUrl=" + fromUrl);
        loginRedirectUrl = loginRedirectUrl + (StringUtils.isEmpty(shareCode) ? "" : "&shareCode=" + shareCode);
        return GzhConstants.AUTHORIZE_URL.replace("{APP_ID}", appId).replace("{REDIRECT_URL}", loginRedirectUrl);
    }

    /**
     * 微信公众号授权登录通过code获取access_token和openid
     * @param appId 公众号id
     * @param appSecret 公众号key
     * @param code 微信授权回调所返回的code
     * @return
     */
    public static GzhAuth authGzh(String appId, String appSecret, String code) {
        String accessor = HttpUtils.get(GzhConstants.AUTH_ACCESS_TOKEN_URL.replace("{APP_ID}", appId).replace("{APP_KEY}", appSecret).replace("{CODE}", code));
        GzhAuth gzhAuth = null;
        if (StringUtils.isNotEmpty(accessor) && !accessor.contains(WeixinConstants.ERROR_CODE_STR)) {
            JSONObject accessorJSON = JSON.parseObject(accessor);
            gzhAuth = new GzhAuth();
            gzhAuth.setAccessToken(accessorJSON.getString("access_token"));
            gzhAuth.setOpenid(accessorJSON.getString("openid"));
        }
        return gzhAuth;
    }

    /**
     * 微信小程序授权登录，通过code来获取session_key 和 openid
     * @param appId 小程序id
     * @param appSecret 小程序key
     * @param code 小程序登录时获取的code
     * @return
     */
    public static XcxAuth authXcx(String appId, String appSecret, String code) {
        String accessor = HttpUtils.get(XcxConstants.AUTH_ACCESS_URL.replace("{APP_ID}", appId).replace("{APP_KEY}", appSecret).replace("{JSCODE}", code));
        XcxAuth xcxAuth = null;
        if (StringUtils.isNotEmpty(accessor) && !accessor.contains(WeixinConstants.ERROR_CODE_STR)) {
            JSONObject accessorJSON = JSON.parseObject(accessor);
            xcxAuth = new XcxAuth();
            xcxAuth.setSessionKey(accessorJSON.getString("session_key"));
            xcxAuth.setOpenid(accessorJSON.getString("openid"));
            xcxAuth.setUnionid(accessorJSON.getString("unionid"));
        }
        return xcxAuth;
    }

    /**
     * 通过access_token和openid获取微信用户信息
     * @param gzhAuth WeixinAuth对象
     * @return
     */
    public static WeixinUser userInfo(GzhAuth gzhAuth) {
        WeixinUser weixinUser = null;
        if (gzhAuth != null) {
            String userInfo = HttpUtils.get(GzhConstants.USER_INFO.replace("{ACCESS_TOKEN}", gzhAuth.getAccessToken()).replace("{OPENID}", gzhAuth.getOpenid()));
            if (StringUtils.isNotEmpty(userInfo) && !userInfo.contains(WeixinConstants.ERROR_CODE_STR)) {
                try {
                    userInfo = new String(userInfo.getBytes(CharsetEnum.ISO8859_1.getValue()), CharsetEnum.UTF8.getValue());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONObject userInfoJSON = JSON.parseObject(userInfo);
                weixinUser = new WeixinUser();
                weixinUser.setOpenid(gzhAuth.getOpenid());
                weixinUser.setNickname(userInfoJSON.getString("nickname"));
                weixinUser.setHeadimgurl(userInfoJSON.getString("headimgurl"));
                weixinUser.setSex(userInfoJSON.getString("sex"));
                weixinUser.setCountry(userInfoJSON.getString("country"));
                weixinUser.setProvince(userInfoJSON.getString("province"));
                weixinUser.setCity(userInfoJSON.getString("city"));
                // weixinUser.setPrivilege((String[]) userInfoJSON.getJSONArray("privilege").toArray());
                weixinUser.setUnionid(userInfoJSON.getString("unionid"));
            }
        }
        return weixinUser;
    }

    /**
     * 准备统一下单需要提交的数据
     * @param appId
     * @param mchId
     * @param apiKey
     * @param openid
     * @param orderNo
     * @param ip
     * @param body
     * @param attach
     * @param totalFee
     * @param payNotifyUrl
     * @return
     */
    public static Map<String, String> unifiedOrderData(String appId, String mchId, String apiKey, String openid, String orderNo, String ip, String body, String attach, int totalFee, String payNotifyUrl) {
        Map<String, String> data = new HashMap<>();
        data.put("appid", appId);
        data.put("mch_id", mchId);
        data.put("nonce_str", UUIDUtils.simpleUuid());
        data.put("sign_type", PayConstants.MD5);
        data.put("openid", openid);
        data.put("body", body);
        data.put("attach", attach);
        data.put("out_trade_no", orderNo);
        data.put("total_fee", totalFee +"");
        data.put("trade_type", PayConstants.TRADE_JSAPI);
        data.put("spbill_create_ip", ip);
        data.put("notify_url", payNotifyUrl);
        try {
            data.put("sign", WXPayUtil.generateSignature(data, apiKey, WXPayConstants.SignType.MD5));
        } catch (Exception e) {
            logger.error("unifiedOrderData error: {}", e.getMessage());
        }
        return data;
    }

    /**
     * 统一下单
     * @param appId
     * @param mchId
     * @param apiKey
     * @param openid
     * @param orderNo
     * @param ip
     * @param body
     * @param attach
     * @param totalFee
     * @param payNotifyUrl
     * @return
     */
    public static Map<String, String> unifiedOrder(String appId, String mchId, String apiKey, String openid, String orderNo, String ip, String body, String attach, int totalFee, String payNotifyUrl) {
        Map<String, String> unifiedOrderData = unifiedOrderData(appId, mchId, apiKey, openid, orderNo, ip, body, attach, totalFee, payNotifyUrl);
        try {
            String unifiedOrderResult = HttpUtils.postXML(PayConstants.UNIFIED_ORDER_URL, WXPayUtil.mapToXml(unifiedOrderData));
            if (unifiedOrderResult != null) {
                unifiedOrderResult = new String(unifiedOrderResult.getBytes(CharsetEnum.ISO8859_1.getValue()), CharsetEnum.UTF8.getValue());
                return WXPayUtil.xmlToMap(unifiedOrderResult);
            }
        } catch (Exception e) {
            logger.error("unifiedOrder error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 准备发起支付的数据
     * @param appId
     * @param apiKey
     * @param unifiedOrderResult
     * @return
     */
    public static Map<String, String> payDataMap(String appId, String apiKey, Map<String, String> unifiedOrderResult) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("appId", appId);
        data.put("package", "prepay_id=" + unifiedOrderResult.get("prepay_id"));
        data.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
        data.put("nonceStr", UUIDUtils.simpleUuid());
        data.put("signType", PayConstants.MD5);
        try {
            data.put("paySign", WXPayUtil.generateSignature(data, apiKey, WXPayConstants.SignType.MD5));
        } catch (Exception e) {
            logger.error("payDataMap error: {}", e.getMessage());
        }
        return data;
    }

    /**
     * 由payDataMap生成PayData对象
     * @param payDataMap
     * @return
     */
    public static PayData payData(Map<String, String> payDataMap) {
        PayData payData = new PayData();
        payData.setAppId(payDataMap.get("appId"));
        payData.setTimeStamp(payDataMap.get("timeStamp"));
        payData.setNonceStr(payDataMap.get("nonceStr"));
        payData.setPackages(payDataMap.get("package"));
        payData.setPaySign(payDataMap.get("paySign"));
        return payData;
    }

    /**
     * 读取由微信支付服务器传回的支付结果
     * @param request
     * @return
     */
    public static PayResult payResult(HttpServletRequest request) {
        try {
            ServletInputStream in = request.getInputStream();
            byte[] bytes = new byte[1024];
            int total = 0;
            StringBuilder result = new StringBuilder();
            while ((total = in.read(bytes)) != -1) {
                result.append(new String(bytes, 0, total));
            }
            Map<String, String> payResultMap = WXPayUtil.xmlToMap(result.toString());;
            PayResult payResult = new PayResult();
            payResult.setResultCode(payResultMap.get("result_code"));
            payResult.setAppId(payResultMap.get("appid"));
            payResult.setMchId(payResultMap.get("mch_id"));
            payResult.setOpenid(payResultMap.get("openid"));
            payResult.setOutTradeNo(payResultMap.get("out_trade_no"));
            payResult.setTransactionId(payResultMap.get("transaction_id"));
            payResult.setTotalFee(Integer.valueOf(payResultMap.get("total_fee")));
            payResult.setTradeType(payResultMap.get("trade_type"));
            payResult.setTimeEnd(payResultMap.get("time_end"));
            return payResult;
        } catch (Exception e) {
            logger.error("payResult error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 响应微信支付结果
     * @param response
     */
    public static void responsePayNotify(HttpServletResponse response) {
        response.setContentType(ContentTypeEnum.XML.getValue());
        try {
            response.getWriter().write(PayConstants.PAY_NOTIFY_RESULT);
        } catch (IOException e) {
            logger.error("responsePayNotify error: {}", e.getMessage());
        }
    }

    /**
     * 根据sessionKey，加密数据和iv向量解密手机号信息
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return
     */
    public static XcxPhone decryptPhoneData(String sessionKey, String encryptedData, String iv) {
        Base64.Decoder decoder = Base64.getDecoder();
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(decoder.decode(iv));
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(decoder.decode(sessionKey), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            String dataStr = new String(cipher.doFinal(decoder.decode(encryptedData)),CharsetEnum.UTF8.getValue());
            return IOUtils.readJsonStrToObject(dataStr, XcxPhone.class);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            logger.error("decryptData error: {}", e.getMessage());
        }
        return null;
    }
}
