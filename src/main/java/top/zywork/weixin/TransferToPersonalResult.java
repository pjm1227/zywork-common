package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信支付企业付款到个人结果对象类<br/>
 *
 * 创建于2019-06-29<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferToPersonalResult {

    /**
     * 业务码
     */
    private String resultCode;
    /**
     * 商户绑定的appid
     */
    private String mchAppid;
    /**
     * 商户id
     */
    private String mchId;
    /**
     * 商户订单号
     */
    private String partnerTradeNo;
    /**
     * 微信付款单号
     */
    private String paymentNo;
    /**
     * 支付时间
     */
    private String paymentTime;

}
