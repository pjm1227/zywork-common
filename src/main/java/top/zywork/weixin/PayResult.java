package top.zywork.weixin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信支付结果对象类<br/>
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
public class PayResult {

    /**
     * 业务码
     */
    private String resultCode;
    /**
     * appid
     */
    private String appId;
    /**
     * 商户id
     */
    private String mchId;
    /**
     * 用户openid
     */
    private String openid;
    /**
     * 支付订单号
     */
    private String transactionId;
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 支付金额
     */
    private Integer totalFee;
    /**
     * 交易类型
     */
    private String tradeType;
    /**
     * 交易完成时间
     */
    private String timeEnd;

}
