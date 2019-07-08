package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 小程序模板消息实体类<br/>
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
public class XcxTemplateMsg {

    /**
     * 接收消息用户openid，必填，发送统一消息时不需要填写
     */
    private String touser;
    /**
     * 消息模板编号，必填
     */
    private String template_id;
    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面，非必填
     */
    private String page;
    /**
     * 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id，必填
     */
    private String form_id;
    /**
     * 消息数据，必填
     */
    private Map<String, MsgData> data;
    /**
     * 模板需要放大的关键词，不填则默认无放大，非必填
     */
    private String emphasis_keyword;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MsgData {
        /**
         * 消息值
         */
        private String value;
    }

}