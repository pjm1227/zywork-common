package top.zywork.weixin;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import top.zywork.common.HttpUtils;
import top.zywork.common.UUIDUtils;
import top.zywork.enums.CertTypeEnum;
import top.zywork.enums.ContentTypeEnum;
import top.zywork.enums.MIMETypeEnum;
import top.zywork.enums.SSLProtocolEnum;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付相关工具类<br/>
 *
 * 创建于2018-12-04<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Slf4j
public class WeixinPayUtils {

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
            log.error("unifiedOrderData error: {}", e.getMessage());
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
            log.error("unifiedOrder error: {}", e.getMessage());
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
            log.error("payDataMap error: {}", e.getMessage());
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
            log.error("payResult error: {}", e.getMessage());
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
            log.error("responsePayNotify error: {}", e.getMessage());
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
            log.error("redpackData error: {}", e.getMessage());
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
     * @param certPath 证书路径
     * @return 发送红包的结果 Map
     */
    public static Map<String, String> sendRedpackGzh(String appid, String mchId, String apiKey, String openid, String ip, String mchBillNo,
                                                     String sendName, int totalAmount, int totalNum, String wishing, String actName, String remark, String sceneId, String certPath) {
        Map<String, String> redpackData = redpackData(appid, mchId, apiKey, openid, ip, mchBillNo, sendName, totalAmount, totalNum, wishing, actName, remark, sceneId);
        try {
            String redpackResult = HttpUtils.post(PayConstants.SEND_RED_PACK_GZH, WXPayUtil.mapToXml(redpackData), MIMETypeEnum.XML,
                    certPath, mchId, CertTypeEnum.PKCS12.getValue(), new String[]{SSLProtocolEnum.TLSv1.getValue()});
            if (redpackResult != null) {
                return WXPayUtil.xmlToMap(redpackResult);
            }
        } catch (Exception e) {
            log.error("sendRedpackGzh error: {}", e.getMessage());
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
     * 为企业付款到个人准备数据
     * @param mchAppid
     * @param mchId
     * @param apiKey
     * @param openid
     * @param ip
     * @param partnerTradeNo
     * @param amount
     * @param checkName
     * @param desc
     * @return
     */
    public static Map<String, String> transferToPersonalData(String mchAppid, String mchId, String apiKey, String openid,
                                                             String ip, String partnerTradeNo, int amount, String checkName, String desc) {
        Map<String, String> data = new HashMap<>();
        data.put("mch_appid", mchAppid);
        data.put("mchid", mchId);
        data.put("nonce_str", UUIDUtils.simpleUuid());
        data.put("openid", openid);
        data.put("spbill_create_ip", ip);
        data.put("partner_trade_no", partnerTradeNo);
        data.put("amount", amount + "");
        data.put("check_name", checkName);
        data.put("desc", desc);
        try {
            data.put("sign", WXPayUtil.generateSignature(data, apiKey, WXPayConstants.SignType.MD5));
        } catch (Exception e) {
            log.error("transfer to personal data error: {}", e.getMessage());
        }
        return data;
    }

    /**
     * 微信企业付款到个人
     * @param mchAppid 申请商户号的appid或商户号绑定的appid
     * @param mchId 商户id
     * @param apiKey 商户apiket
     * @param openid 用户openid
     * @param ip 调用接口的ip地址
     * @param partnerTradeNo 商户订单号
     * @param amount 支付总金额，单位为分
     * @param checkName 是否校验用户真实姓名，NO_CHECK：不校验真实姓名，FORCE_CHECK：强校验真实姓名
     * @param desc 企业付款备注
     */
    public static Map<String, String> transferToPersonal(String mchAppid, String mchId, String apiKey, String openid, String ip,
                                                         String partnerTradeNo, int amount, String checkName, String desc, String certPath) {
        Map<String, String> transferToPersonalData = transferToPersonalData(mchAppid, mchId, apiKey, openid, ip,partnerTradeNo, amount, checkName, desc);
        try {
            String transferResult = HttpUtils.post(PayConstants.TRANSFER_TO_PERSONAL, WXPayUtil.mapToXml(transferToPersonalData), MIMETypeEnum.XML,
                    certPath, mchId, CertTypeEnum.PKCS12.getValue(), new String[]{SSLProtocolEnum.TLSv1.getValue()});
            if (transferResult != null) {
                return WXPayUtil.xmlToMap(transferResult);
            }
        } catch (Exception e) {
            log.error("transferToPersonal error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 把微信企业付款的结果Map转化成TransferToPersonalResult对象
     * @param transferToPersonalResultMap
     * @return
     */
    public static TransferToPersonalResult transferToPersonalResult(Map<String, String> transferToPersonalResultMap) {
        TransferToPersonalResult transferToPersonalResult = new TransferToPersonalResult();
        transferToPersonalResult.setResultCode(transferToPersonalResultMap.get("result_code"));
        transferToPersonalResult.setMchAppid(transferToPersonalResultMap.get("mch_appid"));
        transferToPersonalResult.setMchId(transferToPersonalResultMap.get("mchid"));
        transferToPersonalResult.setPartnerTradeNo(transferToPersonalResultMap.get("partner_trade_no"));
        transferToPersonalResult.setPaymentNo(transferToPersonalResultMap.get("payment_no"));
        transferToPersonalResult.setPaymentTime(transferToPersonalResultMap.get("payment_time"));
        return transferToPersonalResult;
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

}
