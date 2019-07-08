package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小程序端统一消息实体类<br/>
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
public class UniformMsg {
    /**
     * 接收统一消息的用户openid
     */
    private String touser;
    /**
     * 小程序模板消息，非必须。请认真阅读属性说明
     */
    private XcxTemplateMsg weapp_template_msg;
    /**
     * 公众号模板消息，必须。请认真阅读属性说明
     */
    private GzhTemplateMsg mp_template_msg;

}
