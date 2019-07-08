package top.zywork.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import top.zywork.common.HttpUtils;
import top.zywork.common.IOUtils;
import top.zywork.common.RedisUtils;
import top.zywork.enums.CharsetEnum;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * 微信登录和获取微信信息相关工具类<br/>
 *
 * 创建于2018-12-04<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Slf4j
public class WeixinAuthUtils {


    public static final String EXTRA_PARAMS = "extraParams";

    public static final String EXTRA_PARAMS_SEPARATOR = "__";

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
     * 通过access_token和openid获取微信用户信息，如果用户关注了公众号，可获取unionid
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
                weixinUser.setUnionid(userInfoJSON.getString("unionid"));
                weixinUser.setSubscribe(userInfoJSON.getInteger("subscribe"));
            }
        }
        return weixinUser;
    }

    /**
     * 微信小程序授权登录，通过code来获取session_key 和 openid。此种方式如有在用户关注了同主体的公众号才能获取到unionid
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
     * 根据sessionKey，加密数据和iv向量解密手机号信息
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return
     */
    public static XcxPhone decryptPhoneData(String sessionKey, String encryptedData, String iv) {
        Cipher cipher = getCipher(sessionKey, iv);
        if (cipher != null) {
            try {
                String dataStr = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)), CharsetEnum.UTF8.getValue());
                return IOUtils.readJsonStrToObject(dataStr, XcxPhone.class);
            } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
                log.error("decryptData error: {}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 从小程序获取的用户敏感信息中解析unionid
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return
     */
    public static String decryptUnionid(String sessionKey, String encryptedData, String iv) {
        Cipher cipher = getCipher(sessionKey, iv);
        if (cipher != null) {
            try {
                String dataStr = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)), CharsetEnum.UTF8.getValue());
                JSONObject jsonObject = JSONObject.parseObject(dataStr);
                return jsonObject.get("unionId").toString();
            } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
                log.error("decryptData error: {}", e.getMessage());
            }
        }
        return null;
    }

    private static Cipher getCipher(String sessionKey, String iv) {
        Base64.Decoder decoder = Base64.getDecoder();
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(decoder.decode(iv));
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(decoder.decode(sessionKey), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            log.error("getCipher error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取微信access_token，并存储到Redis缓存中，缓存时间为115分钟
     * @param redisTemplate
     * @param accessTokenKey
     * @param appId
     * @param appKey
     * @param weixinType 0表示微信公众号，1表示微信小程序
     * @return
     */
    public static String getAccessToken(RedisTemplate<String, Object> redisTemplate, String accessTokenKey, String appId, String appKey, int weixinType) {
        if (RedisUtils.exists(redisTemplate, accessTokenKey)) {
            return RedisUtils.get(redisTemplate, accessTokenKey).toString();
        }
        String accessTokenApi = "";
        if (weixinType == 0) {
            accessTokenApi = GzhConstants.ACCESS_TOKEN_URL;
        } else if (weixinType == 1){
            accessTokenApi = XcxConstants.ACCESS_TOKEN_URL;
        }
        String response = HttpUtils.get(accessTokenApi.replace("{APP_ID}", appId).replace("{APP_KEY}", appKey));
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (isResultOk(jsonObject)) {
            String accessToken = jsonObject.get("access_token").toString();
            RedisUtils.save(redisTemplate, accessTokenKey, accessToken, 115, TimeUnit.MINUTES);
            return accessToken;
        }
        log.error("get access token error: {}", errmsg(jsonObject));
        return null;
    }

    public static boolean isResultOk(JSONObject resultJsonResult) {
        Object errcode = resultJsonResult.get(WeixinConstants.ERROR_CODE_STR);
        return  errcode != null && Integer.valueOf(errcode.toString()) == 0;
    }

    public static String errmsg(JSONObject resultJsonResult) {
        return resultJsonResult.get(WeixinConstants.ERROR_MSG_STR).toString();
    }

}
