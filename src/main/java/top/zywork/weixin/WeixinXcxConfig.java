package top.zywork.weixin;

import lombok.*;

/**
 * 微信小程序相关配置类<br/>
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
public class WeixinXcxConfig extends WeixinBaseConfig {

    /**
     * 公众号id
     */
    private String appId;
    /**
     * 公众号key
     */
    private String appSecret;

}
