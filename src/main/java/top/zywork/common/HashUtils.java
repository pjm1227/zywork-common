package top.zywork.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.enums.HashAlgorithmEnum;
import top.zywork.enums.CharsetEnum;
import top.zywork.enums.HashEncodeEnum;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类<br/>
 * 创建于2017-08-15<br/>
 * 修改于2017-09-04，增加salt<br/>
 *
 * @author 王振宇
 * @version 1.1
 * @see HashAlgorithmEnum
 */
public class HashUtils {

    private static final Logger logger = LoggerFactory.getLogger(HashUtils.class);

    /**
     * 不使用盐值的md5加密
     * @param str 明文
     * @param hashEncodeEnum 指定的编码方式
     * @return 使用MD5加密算法得到的密文
     */
    public static String md5(String str, HashEncodeEnum hashEncodeEnum) {
        String encryptStr = null;
        try {
            encryptStr = oneWayEncrypt(str, "",  HashAlgorithmEnum.MD5.getValue(), hashEncodeEnum);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("md5 hash error: {}", e.getMessage());
        }
        return encryptStr;
    }

    /**
     * 使用盐值的md5加密
     * @param str 明文
     * @param salt 盐值
     * @param hashEncodeEnum 指定的编码方式
     * @return 使用md5加密算法并加入盐值加密得到的密文
     */
    public static String md5(String str, String salt, HashEncodeEnum hashEncodeEnum) {
        String encryptStr = null;
        try {
            encryptStr = oneWayEncrypt(str, salt, HashAlgorithmEnum.MD5.getValue(), hashEncodeEnum);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("md5 hash error: {}", e.getMessage());
        }
        return encryptStr;
    }

    /**
     * 不使用盐值的sha1加密
     * @param str 明文
     * @param hashEncodeEnum 指定的编码方式
     * @return 使用sha1加密算法得到的密文
     */
    public static String sha1(String str, HashEncodeEnum hashEncodeEnum) {
        String encryptStr = null;
        try {
            encryptStr = oneWayEncrypt(str, "", HashAlgorithmEnum.SHA1.getValue(), hashEncodeEnum);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("sha1 hash error: {}", e.getMessage());
        }
        return encryptStr;
    }

    /**
     * 使用盐值的sha1加密
     * @param str 明文
     * @param salt 盐值
     * @param hashEncodeEnum 指定的编码方式
     * @return 使用sha1加密算法并加入盐值加密得到的密文
     */
    public static String sha1(String str, String salt, HashEncodeEnum hashEncodeEnum) {
        String encryptStr = null;
        try {
            encryptStr = oneWayEncrypt(str, salt,  HashAlgorithmEnum.SHA1.getValue(), hashEncodeEnum);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("sha1 hash error: {}", e.getMessage());
        }
        return encryptStr;
    }

    /**
     * 不使用盐值的sha256加密
     * @param str 明文
     * @param hashEncodeEnum 指定的编码方式
     * @return 使用sha1加密算法得到的密文
     */
    public static String sha256(String str, HashEncodeEnum hashEncodeEnum) {
        String encryptStr = null;
        try {
            encryptStr = oneWayEncrypt(str, "", HashAlgorithmEnum.SHA256.getValue(), hashEncodeEnum);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("sha256 hash error: {}", e.getMessage());
        }
        return encryptStr;
    }

    /**
     * 使用盐值的sha256加密
     * @param str 明文
     * @param salt 盐值
     * @param hashEncodeEnum 指定的编码方式
     * @return 使用sha1加密算法并加入盐值加密得到的密文
     */
    public static String sha256(String str, String salt, HashEncodeEnum hashEncodeEnum) {
        String encryptStr = null;
        try {
            encryptStr = oneWayEncrypt(str, salt,  HashAlgorithmEnum.SHA256.getValue(), hashEncodeEnum);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("sha256 hash error: {}", e.getMessage());
        }
        return encryptStr;
    }

    /**
     * 单向加密，可指定编码方式，如果未指定编码方式，则默认使用十六进制编码
     * @param str 需要加密的明文
     * @param salt 加密所使用的盐值
     * @param type 加密或消息摘要算法名称
     * @param hashEncodeEnum 指定的编码方式
     * @return 通过指定加密算法和盐值得到的密文
     * @throws NoSuchAlgorithmException 未知的算法
     * @throws UnsupportedEncodingException 不支持的编码方式
     */
    public static String oneWayEncrypt(String str, String salt, String type, HashEncodeEnum hashEncodeEnum)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(type);
        byte[] bytes = md.digest((str + salt).getBytes(CharsetEnum.UTF8.getValue()));
        if (hashEncodeEnum == HashEncodeEnum.HEX) {
            return ByteUtils.toHex(bytes);
        } else if (hashEncodeEnum == HashEncodeEnum.BASE64) {
            return ByteUtils.toBase64(bytes);
        } else {
            return ByteUtils.toHex(bytes);
        }
    }

}
