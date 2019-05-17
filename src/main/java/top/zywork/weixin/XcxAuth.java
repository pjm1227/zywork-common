package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储微信小程序授权获取的session_key与openid的对象<br/>
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
public class XcxAuth {

    private String sessionKey;
    private String openid;
    private String unionid;

}
