package top.zywork.common;

import top.zywork.enums.RadixEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的字符串工具类<br/>
 *
 * 创建于2018-03-12<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class StringUtils {

    /**
     * 字符串转换成ASCII码，可以指定分割符连接转换后的ASCII码，并且可以指定进制数来转换
     * @param str 需要转换成ASCII码的字符串
     * @param separator 分割符，如0x
     * @param radixEnum 进制枚举
     * @return 通过指定分割符及进制转换的ASCII码字符串
     */
    public static String strToAscii(String str, String separator, RadixEnum radixEnum) {
        char[] chars = str.toCharArray();
        StringBuilder ascii = new StringBuilder("");
        for (char c : chars) {
            ascii.append(separator);
            String cAscii = "";
            if (radixEnum == RadixEnum.BINARY) {
                cAscii = Integer.toBinaryString(c);
            } else if (radixEnum == RadixEnum.OCTAL) {
                cAscii = Integer.toOctalString(c);
            } else if (radixEnum == RadixEnum.HEX) {
                cAscii = Integer.toHexString(c);
            } else {
                cAscii = Integer.toString(c);
            }
            ascii.append(cAscii);
        }
        return ascii.toString();
    }

    /**
     * ASCII码转换成字符串，可以指定分割符，如果未指定分割符，将没法正确拆分原始ASCII码字符串。并可以指定进制数
     * @param ascii 需要转换成普通字符串的ASCII码字符串
     * @param separator 分割符，如0x
     * @param radixEnum 进制枚举
     * @return 通过指定分割符及进制转换的普通字符串
     */
    public static String asciiToStr(String ascii, String separator, RadixEnum radixEnum) {
        String[] chars = ascii.split(separator);
        StringBuilder str = new StringBuilder("");
        for (String c : chars) {
            if (!c.equals("")) {
                str.append((char) Integer.parseInt(c, radixEnum.getValue()));
            }
        }
        return str.toString();
    }

    /**
     * 把指定符号分割的数字字符串转成长整型数字数组
     * @param str 指定符号分割的长整型数字字符串
     * @return 长整型数字数组
     */
    public static Long[] strToLongArray(String str, String separator) {
        return strArrayToLongArray(str.split(separator));
    }

    /**
     * 把字符器中数组转化成对应的长整型数组
     * @param strArray 字符串数组
     * @return 长整型数组
     */
    public static Long[] strArrayToLongArray(String[] strArray) {
        Long[] longArray = new Long[strArray.length];
        for (int i = 0, len = longArray.length; i < len; i++) {
            longArray[i] = Long.valueOf(strArray[i]);
        }
        return longArray;
    }

    /**
     * 把指定符号分割的数字字符串转成整型数字数组
     * @param str 指定符号分割的整型数字字符串
     * @return 整型数字数组
     */
    public static Integer[] strToIntegerArray(String str, String separator) {
        String[] strArray = str.split(separator);
        Integer[] integerArray = new Integer[strArray.length];
        for (int i = 0, len = strArray.length; i < len; i++) {
            integerArray[i] = Integer.valueOf(strArray[i]);
        }
        return integerArray;
    }

    /**
     * 判断一个字符串是否出现在指定的字符串数组中，忽略大小写
     * @param strArray 字符串数组
     * @param str 字符串
     * @return 如果字符串出现在指定的字符串数组中，返回true，否则返回false
     */
    public static boolean isInArray(String[] strArray, String str) {
        return isInArray(strArray, str, true);
    }

    /**
     * 判断一个字符串是否出现在指定的字符串数组中，可指定是否忽略大小写
     * @param strArray 字符串数组
     * @param str 字符串
     * @param ignoreCase 是否忽略大小写
     * @return 如果字符串出现在指定的字符串数组中，返回true，否则返回false
     */
    public static boolean isInArray(String[] strArray, String str, boolean ignoreCase) {
        for (String s : strArray) {
            if ((ignoreCase && s.equalsIgnoreCase(str)) || (!ignoreCase && s.equals(str))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取字符串对应的字符列表
     * @param src 源字符串
     * @return 字符串对应的字符列表
     */
    public static List<Character> stringToCharList(String src) {
        char[] chars = src.toCharArray();
        List<Character> characters = new ArrayList<>();
        for (char c : chars) {
            characters.add(c);
        }
        return characters;
    }

    /**
     * 字符列表转字符串
     * @param characters 字符列表
     * @return 字符串
     */
    public static String charListToString(List<Character> characters) {
        StringBuilder sb = new StringBuilder("");
        for (Character c : characters) {
            sb.append(c);
        }
        return sb.toString();
    }

}
