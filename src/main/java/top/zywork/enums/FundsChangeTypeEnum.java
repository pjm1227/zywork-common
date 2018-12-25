package top.zywork.enums;

/**
 * 资金变动类型枚举<br/>
 * 创建于2018-12-22<br/>
 *
 * @author 王振宇
 * @version 1.0
 * @see org.apache.http.entity.ContentType
 * @see MIMETypeEnum
 */
public enum FundsChangeTypeEnum {

    RECHARGE_WXPAY("微信充值"),
    RECHARGE_ALIPAY("支付宝充值"),
    RECHARGE_UNIONPAY("银联充值"),
    RECHARGE_HUMAN("人工充值"),
    WITHDRAW("提现"),
    TRANSFER_IN("转入"),
    TRANSFER_OUT("转出"),
    FREZEE("冻结"),
    UNFREZEE("解冻"),
    CONSUME("消费"),
    REFUND("还款"),
    OTHER_IN("其他收入"),
    OTHER_OUT("其他支出");

    private String value;

    FundsChangeTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
