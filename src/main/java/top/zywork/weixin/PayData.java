package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信支付jsapi发起支付需要的数据对象<br/>
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
public class PayData {

    private String appId;
    private String timeStamp;
    private String nonceStr;
    private String packages;
    private String paySign;

}
