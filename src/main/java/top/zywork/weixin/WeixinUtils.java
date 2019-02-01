package top.zywork.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static final String EXTRA_PARAMS = "extraParams";

    public static final String EXTRA_PARAMS_SEPERATOR = "__";

    /**
     * 从哪个url进入到公众号授权界面
     * @param appId
     * @param loginRedirectUrl
     * @param extraParams http://zywork.top/test.html__notAuthUrl__shareCode
     * @return
     */
    public static String gzhAuthorizeUrl(String appId, String loginRedirectUrl, String extraParams) {
        loginRedirectUrl += "?" + EXTRA_PARAMS + "=" + extraParams;
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
        Map<String, String> data = new HashMap<>();
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
     * 读取由微信支付服务器传回的支付结果Map
     * @param request
     * @return
     */
    public static Map<String, String> payResultMap(HttpServletRequest request) {
        try {
            ServletInputStream in = request.getInputStream();
            byte[] bytes = new byte[1024];
            int total;
            StringBuilder result = new StringBuilder();
            while ((total = in.read(bytes)) != -1) {
                result.append(new String(bytes, 0, total));
            }
            return WXPayUtil.xmlToMap(result.toString());
        } catch (Exception e) {
            logger.error("payResult error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 读取由微信支付服务器传回的支付结果PayResult
     * @param payResultMap
     * @return
     */
    public static PayResult payResult(Map<String, String> payResultMap) {
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
     * 准备红包发送需要的数据
     * @param appid 公众号appid
     * @param mchId 商户id
     * @param apiKey 商户apikey
     * @param openid 用户openid
     * @param ip 调用接口的ip地址
     * @param mchBillNo 商户订单号
     * @param sendName 发送名称，如**公司
     * @param totalAmount 发送金额，普通红包1-200元
     * @param totalNum 发送人数
     * @param wishing 祝福语
     * @param actName 活动名称
     * @param remark 备注
     * @param sceneId 普通红包不需要传，非普通红包必传，参考RedpackSceneEnum枚举
     * @return
     */
    public static Map<String, String> redpackData(String appid, String mchId, String apiKey, String openid, String ip, String mchBillNo,
                                                  String sendName, int totalAmount, int totalNum, String wishing, String actName, String remark, String sceneId) {
        Map<String, String> data = new HashMap<>();
        data.put("wxappid", appid);
        data.put("mch_id", mchId);
        data.put("nonce_str", UUIDUtils.simpleUuid());
        data.put("re_openid", openid);
        data.put("client_ip", ip);
        data.put("mch_billno", mchBillNo);
        data.put("send_name", sendName);
        data.put("total_amount", totalAmount + "");
        data.put("total_num", totalNum + "");
        data.put("wishing", wishing);
        data.put("act_name", actName);
        data.put("remark", remark);
        if (sceneId != null) {
            data.put("scene_id", sceneId);
        }
        try {
            data.put("sign", WXPayUtil.generateSignature(data, apiKey, WXPayConstants.SignType.MD5));
        } catch (Exception e) {
            logger.error("redpackData error: {}", e.getMessage());
        }
        return data;
    }

    /**
     * 准备红包发送需要的数据
     * @param appid 公众号appid
     * @param mchId 商户id
     * @param apiKey 商户apikey
     * @param openid 用户openid
     * @param ip 调用接口的ip地址
     * @param mchBillNo 商户订单号
     * @param sendName 发送名称，如**公司
     * @param totalAmount 发送金额，普通红包1-200元
     * @param totalNum 发送人数
     * @param wishing 祝福语
     * @param actName 活动名称
     * @param remark 备注
     * @param sceneId 非普通红包必传，参考RedpackSceneEnum枚举
     * @return 发送红包的结果 Map
     */
    public static Map<String, String> sendRedpack(String appid, String mchId, String apiKey, String openid, String ip, String mchBillNo,
                                   String sendName, int totalAmount, int totalNum, String wishing, String actName, String remark, String sceneId) {
        Map<String, String> redpackData = redpackData(appid, mchId, apiKey, openid, ip, mchBillNo, sendName, totalAmount, totalNum, wishing, actName, wishing, sceneId);
        try {
            String redpackResult = HttpUtils.postXML(PayConstants.SEND_RED_PACK, WXPayUtil.mapToXml(redpackData));
            if (redpackResult != null) {
                redpackResult = new String(redpackResult.getBytes(CharsetEnum.ISO8859_1.getValue()), CharsetEnum.UTF8.getValue());
                return WXPayUtil.xmlToMap(redpackResult);
            }
        } catch (Exception e) {
            logger.error("sendRedpack error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 把红包发送的结果Map转化成RedpackResult对象
     * @param redpackResultMap
     * @return
     */
    public static RedpackResult redpackResult(Map<String, String> redpackResultMap) {
        RedpackResult redpackResult = new RedpackResult();
        redpackResult.setWxappid(redpackResultMap.get("wxappid"));
        redpackResult.setMchId(redpackResultMap.get("mch_id"));
        redpackResult.setMchBillno(redpackResultMap.get("mch_billno"));
        redpackResult.setReOpenid(redpackResultMap.get("re_openid"));
        redpackResult.setResultCode(redpackResultMap.get("result_code"));
        redpackResult.setSendListid(redpackResultMap.get("send_listid"));
        redpackResult.setTotalAmount(Integer.valueOf(redpackResultMap.get("total_amount")));
        return redpackResult;
    }

    /**
     * 判断支付请求结果是否成功
     * @param responseMap
     * @return
     */
    public static boolean isReturnSuccess(Map<String, String> responseMap) {
        return responseMap != null && responseMap.get(PayConstants.RETURN_CODE).equals(PayConstants.RETURN_SUCCESS);
    }

    /**
     * 判断支付业务结果是否成功
     * @param responseMap
     * @return
     */
    public static boolean isResultSuccess(Map<String, String> responseMap) {
        return responseMap != null && responseMap.get(PayConstants.RESULT_CODE).equals(PayConstants.RETURN_SUCCESS);
    }

    /**
     * 获取支付请求结果错误码
     * @param responseMap
     * @return
     */
    public static String errCode(Map<String, String> responseMap) {
        return responseMap.get(PayConstants.ERR_CODE);
    }

    /**
     * 获取支付请求结果错误码描述
     * @param responseMap
     * @return
     */
    public static String errCodeDes(Map<String, String> responseMap) {
        return responseMap.get(PayConstants.ERR_CODE_DES);
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
