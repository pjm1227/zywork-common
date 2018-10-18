package top.zywork.common;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类，获取客户端真实的IP地址<br />
 * 创建于2017-12-19<br />
 *
 * @author 王振宇
 * @version 1.0
 */
public class IPUtils {

    /**
     * 根据HttpServletRequest对象获取客户端真实的IP地址
     * @param request HttpServletRequest对象
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (invalidIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (invalidIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (invalidIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (invalidIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (invalidIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean invalidIp(String ip) {
        return StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
    }
}
