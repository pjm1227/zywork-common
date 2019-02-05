package top.zywork.common;

import top.zywork.enums.DatePatternEnum;

/**
 * 交易编号生成器<br/>
 *
 * 创建于2019-02-05 <br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class TransactionNoGenerator {

    /**
     * 以时间和随机数字生成交易编号
     * @return
     */
    public static String generateNo() {
        return DateFormatUtils.format(System.currentTimeMillis(), DatePatternEnum.DATETIME_SIMPLE.getValue())
                + RandomUtils.randomNum(100000, 999999);
    }

    /**
     * 生成订单编号
     * @return
     */
    public static String generateOrderNo() {
        return generateNo();
    }

}
