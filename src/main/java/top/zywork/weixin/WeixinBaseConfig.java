package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信配置基础类<br/>
 * 创建于2018-12-17<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeixinBaseConfig {

    /**
     * base url
     */
    private String baseUrl;

}
