package top.zywork.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean相关工具类，如bean的拷贝，依赖于spring-beans<br/>
 * 创建于2018-05-25<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class BeanUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 把源对象拷贝到目标对象
     * @param source 源对象
     * @param target 目标对象
     * @param <E> 源对象泛型
     * @param <T> 目标对象泛型
     * @return
     */
    public static <E, T> T copy(E source, T target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 把源对象拷贝到目标对象
     * @param source 源对象
     * @param targetClass 目标对象类
     * @param <E> 源对象泛型
     * @param <T> 目标对象泛型
     * @return
     */
    public static <E, T> T copy(E source, Class<T> targetClass) {
        try {
            T t = targetClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, t);
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Bean copy error, src: {}, target: {}, error: {}", source.getClass(), targetClass, e.getMessage());
            return null;
        }
    }

    /**
     * 把列表中指定的源对象拷贝到另外一个列表中的目标对象，注意：这不是一个简单的List的拷贝，而是List内部对象的重新拷贝
     * @param source 源对象列表
     * @param targetClass 目标对象类
     * @param <E> 源对象泛型
     * @param <T> 目标对象泛型
     * @return
     */
    public static <E, T> List<T> copy(List<E> source, Class<T> targetClass) {
        List<T> targetList = new ArrayList<>();
        for (E e : source) {
            targetList.add(copy(e, targetClass));
        }
        return targetList;
    }

    /**
     * 把集合中指定的源对象拷贝到另外一个集合中的目标对象，注意：这不是一个简单的集合的拷贝，而是集合内部的对象的重新拷贝
     * @param source 源对象列表
     * @param targetClass 目标对象类
     * @return
     */
    public static List<Object> copyList(List<Object> source, Class<?> targetClass) {
        List<Object> targetList = new ArrayList<>();
        for (Object object : source) {
            targetList.add(copy(object, targetClass));
        }
        return targetList;
    }

    /**
     * 把集合中指定的源对象拷贝到另外一个集合中的目标对象，注意：这不是一个简单的集合的拷贝，而是集合内部的对象的重新拷贝
     * @param source 源对象列表
     * @param targetClass 目标对象类
     * @return
     */
    public static <E> List<Object> copyListObj(List<E> source, Class<?> targetClass) {
        List<Object> targetList = new ArrayList<>();
        for (Object object : source) {
            targetList.add(copy(object, targetClass));
        }
        return targetList;
    }

}
