package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信支付发送红包结果对象类<br/>
 *
 * 创建于2019-01-16<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedpackResult {

    /**
     * 业务码
     */
    private String resultCode;
    /**
     * 公众号appid
     */
    private String wxappid;
    /**
     * 商户id
     */
    private String mchId;
    /**
     * 用户openid
     */
    private String reOpenid;
    /**
     * 红包金额
     */
    private int totalAmount;
    /**
     * 商户订单号
     */
    private String mchBillno;
    /**
     * 红包订单号
     */
    private String sendListid;

}
