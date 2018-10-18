package top.zywork.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解，用于方法上，记录系统日志<br/>
 * 创建于2017-12-19<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 用于描述系统日志
     * @return 系统日志描述
     */
    String description() default "";

}
