package top.zywork.common;

/**
 * 银行卡号验证工具类<br/>
 * 创建于2018-12-22<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class BankCardUtils {

    /**
     *  每一个卡号必须通过Luhn算法来验证通过。
     *   该校验的过程：
     *   1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     *   2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，则将其减去9），再求和。
     *   3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     *   例如，卡号是：5432123456788881
     *   则奇数、偶数位（用红色标出）分布：5432123456788881
     *   奇数位和=35
     *   偶数位乘以2（有些要减去9）的结果：1 6 2 6 1 5 7 7，求和=35。
     *   最后35+35=70 可以被10整除，认定校验通过
     * @param cardNo
     * @return
     */
    public static boolean checkBankCard(String cardNo) {
        if(cardNo == null || cardNo.trim().length() == 0 || !cardNo.matches("\\d+")) {
            return false;
        }
        char[] charArray = cardNo.trim().toCharArray();
        int luhmSum = 0;
        for(int i = charArray.length - 1, j = 1; i >= 0; i--, j++) {
            int k = charArray[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return luhmSum % 10 == 0;
    }

}
