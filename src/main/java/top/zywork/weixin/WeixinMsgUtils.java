package top.zywork.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.zywork.common.HttpUtils;

/**
 * 微信消息相关工具类<br/>
 *
 * 创建于2019-07-08<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Slf4j
public class WeixinMsgUtils {

    /**
     * 发送公众号模板消息
     * @param accessToken access_token
     * @param gzhTemplateMsg 公众号模板消息实体
     * @return
     */
    public static boolean sendGzhTemplateMsg(String accessToken, GzhTemplateMsg gzhTemplateMsg) {
        String response = HttpUtils.postJSON(GzhConstants.SEND_TEMPLATE_MSG.replace("{ACCESS_TOKEN}", accessToken), JSON.toJSONString(gzhTemplateMsg));
        log.info("send gzh template msg: {}", response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        return WeixinAuthUtils.isResultOk(jsonObject);
    }

    /**
     * 发送小程序模板消息
     * @param accessToken access_token
     * @param xcxTemplateMsg 小程序模板消息实体
     * @return
     */
    public static boolean sendXcxTemplateMsg(String accessToken, XcxTemplateMsg xcxTemplateMsg) {
        String response = HttpUtils.postJSON(XcxConstants.SEND_TEMPLATE_MSG.replace("{ACCESS_TOKEN}", accessToken), JSON.toJSONString(xcxTemplateMsg));
        log.info("send xcx template msg: {}", response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        return WeixinAuthUtils.isResultOk(jsonObject);
    }

    /**
     * 统一消息
     * @param accessToken access_token
     * @param uniformMsg 统一消息实体
     * @return
     */
    public static boolean sendUniformMsg(String accessToken,UniformMsg uniformMsg) {
        String response = HttpUtils.postJSON(XcxConstants.SEND_UNIFORM_MSG.replace("{ACCESS_TOKEN}", accessToken), JSON.toJSONString(uniformMsg));
        log.info("send uniform template msg: {}", response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        return WeixinAuthUtils.isResultOk(jsonObject);
    }

}
