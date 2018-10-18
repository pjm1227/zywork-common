package top.zywork.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类<br/>
 * 创建于2017-12-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class ReflectUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    /**
     * 调用指定属性的getter方法
     * @param obj 对象
     * @param property 属性名
     * @return getter方法的返回值
     */
    @SuppressWarnings({"unchecked"})
    public static Object invokeGetter(Object obj, String property) {
        Class clazz = obj.getClass();
        try {
            Method method = clazz.getMethod(PropertyUtils.getter(property));
            return method.invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("reflect invoke get method error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 调用指定属性的setter方法
     * @param obj 对象
     * @param property 属性名
     * @param param setter方法的参数
     */
    @SuppressWarnings({"unchecked"})
    public static void invokeSetter(Object obj, String property, Object param) {
        if (param != null) {
            Class clazz = obj.getClass();
            try {
                Method method = clazz.getMethod(PropertyUtils.setter(property), param.getClass());
                method.invoke(obj, param);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error("reflect invoke set method error: {}", e.getMessage());
            }
        }
    }

    /**
     * 反射获取指定对象的属性值
     * @param obj 指定的对象
     * @param property 指定的属性
     * @return 属性值
     */
    public static Object getPropertyValue(Object obj, String property) {
        Class clazz = obj.getClass();
        try {
            Field field = clazz.getDeclaredField(property);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("reflect get property value error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 反射设置指定对象的属性
     * @param obj 指定的对象
     * @param property 指定的属性
     * @param param 属性需要设置的值
     */
    public static void setPropertyValue(Object obj, String property, Object param) {
        Class clazz = obj.getClass();
        try {
            Field field = clazz.getDeclaredField(property);
            field.setAccessible(true);
            field.set(obj, param);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("reflect set property value error: {}", e.getMessage());
        }
    }

}
