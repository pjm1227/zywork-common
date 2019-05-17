package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储微信小程序获取的手机号信息<br/>
 *
 * 创建于2019-01-07<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XcxPhone {

    private String phoneNumber;
    private String purePhoneNumber;
    private String countryCode;
}
