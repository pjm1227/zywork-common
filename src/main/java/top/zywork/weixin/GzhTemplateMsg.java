package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 公众号模板消息实体类<br/>
 *
 * 创建于2019-07-08<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GzhTemplateMsg {

    /**
     * 接收消息的用户openid，必填，发送统一消息时不需要填写
     */
    private String touser;
    /**
     * 小程序同主体下的公众号appid，只有发统一消息时需要，单独发公众号模板消息时不需要此参数
     */
    private String appid;
    /**
     * 消息模板编号，必填
     */
    private String template_id;
    /**
     * 模板跳转链接（海外帐号没有跳转能力），非必填
     */
    private String url;
    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据，非必填
     */
    private Miniprogram miniprogram;
    /**
     * 模板数据
     */
    private Map<String, MsgData> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Miniprogram {
        /**
         * 小程序appid，必填
         */
        private String appid;
        /**
         * 跳转到指定小程序的某个页面，非必填
         */
        private String pagepath;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MsgData {
        /**
         * 消息的值，必填
         */
        private String value;
        /**
         * 消息值的颜色，十六进制颜色值，非必填
         */
        private String color;
    }
}
