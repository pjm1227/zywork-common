package top.zywork.weixin;

import lombok.*;

/**
 * 微信支付相关配置类<br/>
 * 创建于2018-12-17<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WXPayConfig extends WeixinBaseConfig {

    /**
     * 商户id
     */
    private String mchId;
    /**
     * 商户api secret
     */
    private String apiSecret;
    /**
     * 支付通知地址
     */
    private String payNotifyUrl;

}
