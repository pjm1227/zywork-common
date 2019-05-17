package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储微信公众号网页授权登录获取的access_token与openid的对象<br/>
 *
 * 创建于2018-12-04<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GzhAuth {

    /**
     * accessToken
     */
    private String accessToken;
    /**
     * openid
     */
    private String openid;

}
