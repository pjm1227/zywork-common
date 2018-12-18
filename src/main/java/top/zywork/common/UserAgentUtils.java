package top.zywork.common;

import top.zywork.enums.ClientAppEnum;
import top.zywork.enums.ClientOSEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * UserAgent工具类，用于获取访问客户端的信息<br />
 * 创建于2018-12-18<br />
 *
 * @author 王振宇
 * @version 1.0
 */
public class UserAgentUtils {

    public static final String USER_AGENT = "User-Agent";

    public static final String USER_AGENT_WINDOWS = "Windows";

    public static final String USER_AGENT_MAC = "Macintosh";

    public static final String USER_AGENT_IPHONE = "iPhone";

    public static final String USER_AGENT_IPAD = "iPad";

    public static final String USER_AGENT_IPOD = "iPod";

    public static final String USER_AGENT_ANDROID = "Android";

    public static final String USER_AGENT_LINUX = "X11;";

    public static final String USER_AGENT_WEIXIN = "MicroMessenger/";

    public static final String USER_AGENT_QQ = "QQ/";

    /**
     * 根据Http请求获取访问客户端的名称，如iOS，Android，Windows，Mac
     * @param request
     * @return
     */
    public String getClientOsName(HttpServletRequest request) {
        String userAgent = request.getHeader(USER_AGENT);
        if (userAgent.contains(USER_AGENT_WINDOWS)) {
            return ClientOSEnum.WINDOWS.getValue();
        } else if (userAgent.contains(USER_AGENT_MAC)) {
            return ClientOSEnum.MAC.getValue();
        } else if (userAgent.contains(USER_AGENT_IPHONE)
                || userAgent.contains(USER_AGENT_IPAD)
                || userAgent.contains(USER_AGENT_IPOD)) {
            return ClientOSEnum.IOS.getValue();
        } else if (userAgent.contains(USER_AGENT_ANDROID)) {
            return ClientOSEnum.ANDROID.getValue();
        } else if (userAgent.contains(USER_AGENT_LINUX)) {
            return ClientOSEnum.LINUX.getValue();
        }
        return ClientOSEnum.OTHER.getValue();
    }

    /**
     * 根据Http请求获取访问客户端的App名称，如微信和QQ
     * @param request
     * @return
     */
    public String getClientAppName(HttpServletRequest request) {
        String userAgent = request.getHeader(USER_AGENT);
        if (userAgent.contains(USER_AGENT_WEIXIN)) {
            return ClientAppEnum.WEIXIN.getValue();
        } else if (userAgent.contains(USER_AGENT_QQ)) {
            return ClientAppEnum.QQ.getValue();
        }
        return ClientAppEnum.OTHER.getValue();
    }

}
