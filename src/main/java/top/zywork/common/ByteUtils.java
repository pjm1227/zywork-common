package top.zywork.common;

import sun.misc.BASE64Encoder;

/**
 * 字节数组编码工具类<br/>
 *
 * 创建于2018-06-27 <br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class ByteUtils {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 字节数组以十六进制的形式编码成字符串
     * @param bytes 字节数组
     * @return 十六进制编码后的字符串
     */
    public static String toHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) {
            hexChars[index++] = HEX_CHAR[b >>> 4 & 0xF];
            hexChars[index++] = HEX_CHAR[b & 0xF];
        }
        return new String(hexChars);
    }

    /**
     * 字节数组以Base64的形式编码成字符串
     * @param bytes 字节数组
     * @return base64编码后的字符串
     */
    public static String toBase64(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

}
