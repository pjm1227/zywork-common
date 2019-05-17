package top.zywork.common;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类<br/>
 *
 * 创建于2017-12-10<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class RegexUtils {

    /**
     * 中文字符
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
    /**
     * 英文
     */
    public static final String REGEX_ENGLISH = "^[A-Za-z]+$";
    /**
     * 大写英文
     */
    public static final String REGEX_ENGLISH_UPPER = "^[A-Z]+$";
    /**
     * 小写英文
     */
    public static final String REGEX_ENGLISH_LOWER = "^[a-z]+$";
    /**
     * 正负整数
     */
    public static final String REGEX_INTEGER = "^-?[1-9]\\d*$";
    /**
     * 正整数
     */
    public static final String REGEX_INTEGER_POSITIVE = "^[1-9]\\d*$";
    /**
     * 负整数
     */
    public static final String REGEX_INTEGER_NEGTIVE = "^-[1-9]\\d*$";
    /**
     * 正负小数
     */
    public static final String REGEX_DOUBLE = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";
    /**
     * 正小数
     */
    public static final String REGEX_DOUBLE_POSITIVE = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    /**
     * 负小数
     */
    public static final String REGEX_DOUBLE_NEGTIVE = "^-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)$";
    /**
     * 特殊字符
     */
    public static final String REGEX_SPECIAL = "[^%&',;=?$\\x22]+";
    /**
     * ip地址
     */
    public static final String REGEX_IP = "((?:(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d)\\\\.){3}(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d))";
    /**
     * 手机号
     */
    public static final String REGEX_PHONE = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
    /**
     * 邮箱地址
     */
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * 身份证号
     */
    public static final String REGEX_ID_CARD = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
    /**
     * QQ号
     */
    public static final String REGEX_QQ = "[1-9][0-9]{4,14}";
    /**
     * 微信号
     */
    public static final String REGEX_WECHAT = "^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}$";
    /**
     * 电话号码
     */
    public static final String REGEX_TEL = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
    /**
     * 数字
     */
    public static final String REGEX_DECIMAL = "\\-?[1-9]\\d+(\\.\\d+)?";
    /**
     * 空白
     */
    public static final String REGEX_BLANK = "\\s+";
    /**
     * http url地址
     */
    public static final String REGEX_URL = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
    /**
     * 邮政编码
     */
    public static final String REGEX_POSTCODE = "[1-9]\\d{5}";
    /**
     * 日期
     */
    public static final String REGEX_DATE = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
    /**
     * 时间
     */
    public static final String REGEX_TIME = "^(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
    /**
     * 日期时间
     */
    public static final String REGEX_DATETIME = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";

    /**
     * 英文与数字混合的账号
     */
    public static final String REGEX_ACCOUNT = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
    /**
     * 6-16位必须包含有大写字母的密码
     */
    public static final String REGEX_PASSWORD = "^(?!\\d{6,8}$)(?! )(?=.*[a-z])(?=.*[0-9])[a-zA-Z0-9_]{6,16}$";

    /**
     * 验证字符串是否符合正则表达式
     * @param regex
     * @param string
     * @return
     */
    public static boolean match(String regex, String string) {
        return Pattern.matches(regex, string);
    }

}
