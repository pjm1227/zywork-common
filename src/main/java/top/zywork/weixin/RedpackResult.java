package top.zywork.weixin;

/**
 * 微信支付发送红包结果对象类<br/>
 *
 * 创建于2019-01-16<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class RedpackResult {

    // 业务码
    private String resultCode;
    // 公众号appid
    private String wxappid;
    // 商户id
    private String mchId;
    // 用户openid
    private String reOpenid;
    // 红包金额
    private int totalAmount;
    // 商户订单号
    private String mchBillno;
    // 红包订单号
    private String sendListid;

    public RedpackResult() {}

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getWxappid() {
        return wxappid;
    }

    public void setWxappid(String wxappid) {
        this.wxappid = wxappid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getReOpenid() {
        return reOpenid;
    }

    public void setReOpenid(String reOpenid) {
        this.reOpenid = reOpenid;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMchBillno() {
        return mchBillno;
    }

    public void setMchBillno(String mchBillno) {
        this.mchBillno = mchBillno;
    }

    public String getSendListid() {
        return sendListid;
    }

    public void setSendListid(String sendListid) {
        this.sendListid = sendListid;
    }
}
