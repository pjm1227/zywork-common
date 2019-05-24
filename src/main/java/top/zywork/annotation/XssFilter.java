package top.zywork.annotation;

import java.lang.annotation.*;

/**
 * XSS过滤注解，用在controller的方法中，忽略xss过滤，可指定忽略的url<br/>
 * 创建于2019-05-24<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XssFilter {

    /**
     * 忽略xss过滤的url
     * @return 忽略xss过滤的url
     */
    String[] ignoreUrls() default {};

}
