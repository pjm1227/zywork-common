package top.zywork.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.zywork.enums.ContentTypeEnum;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Web工具类，部分方法依赖于spring-web<br />
 * 创建于2017-08-16<br />
 *
 * @author 王振宇
 * @version 1.0
 */
public class WebUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    public static final String SESSION_ID_NAME = "JSESSIONID";

    /**
     * 获取网站根路径
     *
     * @param request HttpServletRequest对象
     * @return 网站根路径
     */
    public static String getRootPath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("/");
    }


    /**
     * 在网站根路径下创建目录，可以是多级目录
     *
     * @param request HttpServletRequest对象
     * @param dirs    在网站根目录下要创建的目录，如static/uploads
     * @return 创建的目录的绝对路径
     */
    public static String mkdirs(HttpServletRequest request, String dirs) {
        return FileUtils.mkdirs(getRootPath(request), dirs);
    }


    /**
     * 获取HTTP请求URL的最后/部分的内容
     *
     * @param request HttpServletRequest对象
     * @return 请求URL中最后/部分的内容
     */
    public static String getReqMethod(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.substring(uri.lastIndexOf("/") + 1);
    }

    /**
     * 把指定的输入流通过response对象的输出流输出
     *
     * @param in  输入流
     * @param out 输出流
     */
    public static void outResponse(InputStream in, OutputStream out) {
        IOUtils.inputToOutput(in, out);
    }

    /**
     * 把指定的输入流通过response对象的输出流输出
     *
     * @param in          输入流
     * @param response    HttpServletResponse响应对象
     * @param contentTypeEnum 响应对象的内容类型枚举
     */
    public static void outResponse(InputStream in, HttpServletResponse response, ContentTypeEnum contentTypeEnum) {
        response.setContentType(contentTypeEnum.getValue());
        try {
            outResponse(in, response.getOutputStream());
        } catch (IOException e) {
            logger.error("web response output stream error: {}", e.getMessage());
        }
    }

    /**
     * 把字符串内容通过指定的内容类型通过response对象输出流输出
     * @param response HttpServletResponse响应对象
     * @param content 字符串内容
     * @param contentTypeEnum 响应对象的内容类型枚举
     */
    public static void outResponse(HttpServletResponse response, String content, ContentTypeEnum contentTypeEnum) {
        try {
            response.setContentType(contentTypeEnum.getValue());
            PrintWriter printWriter = new PrintWriter(response.getOutputStream());
            printWriter.write(content);
        } catch (IOException e) {
            logger.error("response output stream error: {}", e.getMessage());
        }
    }

    /**
     * Spring Web环境中获取HttpServletRequest对象
     *
     * @return HttpServletRequest对象
     */
    public static HttpServletRequest getServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * Spring Web环境中获取HttpServletRequest对象
     *
     * @return HttpServletResponse对象
     */
    public static HttpServletResponse getServletResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }


    /**
     * Spring Web环境中获取ServletContext对象
     *
     * @return ServletContext对象
     */
    public static ServletContext getServletContext() {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext();
    }

    /**
     * Spring Web环境中获取ContextPath应用上下文路径
     *
     * @return 应用上下文路径
     */
    public static String getContextPath() {
        return getServletContext().getContextPath();
    }

    /**
     * 写出cookie
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        response.addCookie(cookie);
    }

    /**
     * 写出cookie
     * @param response
     * @param name
     * @param value
     * @param expiration 失效时间，单位为s
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int expiration) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expiration);
        response.addCookie(cookie);
    }

    /**
     * 根据cookie名称获取Cookie对象
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 根据cookie名称获取cookie值
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

}
