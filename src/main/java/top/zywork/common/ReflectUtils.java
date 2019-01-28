package top.zywork.common;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import top.zywork.annotation.ExposeClass;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 反射工具类<br/>
 * 创建于2017-12-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class ReflectUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    public static final String CLASS_PATH_PREFIX = File.separator + "classes" + File.separator;

    public static final String CLASS_SUFFIX = ".class";

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
            field.setAccessible(true);
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

    /**
     * 获取指定类、方法名和参数类型的所有参数名
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return 如果无参数，返回null
     */
    public static String[] getArgsNames(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            Parameter[] parameters = method.getParameters();
            int parameterCount = parameters.length;
            if (parameterCount == 0) {
                return null;
            }
            String[] argsNames = new String[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                argsNames[i] = parameters[i].getName();
            }
            return argsNames;
        } catch (NoSuchMethodException e) {
            logger.error("getArgsNames error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取指定类中指定方法名的所有参数名称
     * @param clazz
     * @param methodName
     * @return 如果无参数，返回null
     */
    public static String[] getArgsNames(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Parameter[] parameters = method.getParameters();
                int parameterCount = parameters.length;
                if (parameterCount == 0) {
                    return null;
                }
                String[] argsNames = new String[parameterCount];
                for (int i = 0; i < parameterCount; i++) {
                    argsNames[i] = parameters[i].getName();
                }
                return argsNames;
            }
        }
        return null;
    }

    /**
     * 通过spring-core包中相关的类获取指定类中指定方法名的所有参数名
     * @param clazz
     * @param methodName
     * @return
     */
    public static String[] getArgsNamesBySpring(Class<?> clazz, String methodName) {
        ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return parameterNameDiscoverer.getParameterNames(method);
            }
        }
        return null;
    }

    /**
     * 获取指定包中的所有带有指定注解的类名称，如top.zywork.common.Test
     * @param packageName 指定包名
     * @param recursive 是否迭代查询包
     * @param annotation 如果指定了Annotation，则只会去获取带有此Annotation的类信息，如果未指定，为null，则获取所有的类信息
     * @return
     */
    public static List<String> getClassNames(String packageName, Boolean recursive, Class<? extends Annotation> annotation) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        packageName = packageName.replaceAll("\\.", File.separator);
        List<String> classNames = new ArrayList<>();
        try {
            Enumeration<URL> urlEnumeration = classLoader.getResources(packageName);
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    classNames.addAll(getClassNamesFromFile(new File(url.getPath()), recursive, annotation));
                } else if ("jar".equals(protocol)) {
                    classNames.addAll(getClassNamesFromJar(((JarURLConnection) url.openConnection()).getJarFile(), annotation));
                }
            }
            return classNames;
        } catch (IOException e) {
            logger.error("getClassNames error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 以文件的方式获取类信息
     * @param file
     * @param recursive
     * @param annotation
     * @return
     */
    private static List<String> getClassNamesFromFile(File file, Boolean recursive, Class<? extends Annotation> annotation) {
        List<String> classNames = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (!f.isDirectory()) {
                    String classPath = f.getPath();
                    // 过滤掉所有内部类
                    if (classPath.endsWith(CLASS_SUFFIX) && !isInnerClass(classPath)) {
                        String className = classPath.substring(classPath.indexOf(CLASS_PATH_PREFIX) + CLASS_PATH_PREFIX.length())
                                .replace(CLASS_SUFFIX, "")
                                .replaceAll("/", ".");
                        if (isClassWithAnnotation(className, annotation)) {
                            classNames.add(className);
                        }
                    }
                } else {
                    if (recursive) {
                        classNames.addAll(getClassNamesFromFile(f, recursive, annotation));
                    }
                }
            }
        }
        return classNames;
    }

    /**
     * 通过JarFile获取类信息
     * @param jarFile
     * @param annotation
     * @return
     */
    private static List<String> getClassNamesFromJar(JarFile jarFile, Class<? extends Annotation> annotation) {
        List<String> classNames = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 过滤掉所有内部类
            if (name.endsWith(CLASS_SUFFIX) && !isInnerClass(name)) {
                String className = name.replace(CLASS_SUFFIX, "")
                        .replaceAll("/", ".");
                if (isClassWithAnnotation(className, annotation)) {
                    classNames.add(className);
                }
            }
        }
        return classNames;
    }

    /**
     * 判断指定类名的类是否为内部类
     * @param className
     * @return
     */
    public static boolean isInnerClass(String className) {
        return className.contains("$");
    }

    /**
     * 判断指定的类是否为内部类
     * @param clazz
     * @return
     */
    public static boolean isInnerClass(Class<?> clazz) {
        return isInnerClass(clazz.getName());
    }

    /**
     * 判断指定类名的类是否带有指定的注解
     * @param className
     * @param annotation
     * @return
     */
    public static boolean isClassWithAnnotation(String className, Class<? extends Annotation> annotation) {
        if (annotation == null) {
            return true;
        }
        try {
            return isClassWithAnnotation(Class.forName(className), annotation);
        } catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
            logger.error("isClassWithAnnotation error: {}, class name: {}", e.getMessage(), className);
            return false;
        }
    }

    /**
     * 判断指定的类是否带有指定的注解
     * @param clazz
     * @param annotation
     * @return
     */
    public static boolean isClassWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return clazz.isAnnotationPresent(annotation);
    }

    /**
     * 获取带有ExposeClass注解的类，并可指定ExposeClass的type
     * @param packageName
     * @param recursive
     * @param type
     * @return
     */
    public static List<String> getExposeClassNames(String packageName, Boolean recursive, String type) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        packageName = packageName.replaceAll("\\.", File.separator);
        List<String> classNames = new ArrayList<>();
        try {
            Enumeration<URL> urlEnumeration = classLoader.getResources(packageName);
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    classNames.addAll(getExposeClassNamesFromFile(new File(url.getPath()), recursive, type));
                } else if ("jar".equals(protocol)) {
                    classNames.addAll(getExposeClassNamesFromJar(((JarURLConnection) url.openConnection()).getJarFile(), type));
                }
            }
            return classNames;
        } catch (IOException e) {
            logger.error("getExposeClassNames error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从文件中获取带有ExposeClass注解的类，并可指定ExposeClass的type
     * @param file
     * @param recursive
     * @param type
     * @return
     */
    private static List<String> getExposeClassNamesFromFile(File file, Boolean recursive, String type) {
        List<String> classNames = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (!f.isDirectory()) {
                    String classPath = f.getPath();
                    // 过滤掉所有内部类
                    if (classPath.endsWith(CLASS_SUFFIX) && !isInnerClass(classPath)) {
                        String className = classPath.substring(classPath.indexOf(CLASS_PATH_PREFIX) + CLASS_PATH_PREFIX.length())
                                .replace(CLASS_SUFFIX, "")
                                .replaceAll("/", ".");
                        if (isExposeClass(className, type)) {
                            classNames.add(className);
                        }
                    }
                } else {
                    if (recursive) {
                        classNames.addAll(getExposeClassNamesFromFile(f, recursive, type));
                    }
                }
            }
        }
        return classNames;
    }

    /**
     * 从jar文件中获取带有ExposeClass注解的类，并可指定ExposeClass的type
     * @param jarFile
     * @param type
     * @return
     */
    private static List<String> getExposeClassNamesFromJar(JarFile jarFile, String type) {
        List<String> classNames = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 过滤掉所有内部类
            if (name.endsWith(CLASS_SUFFIX) && !isInnerClass(name)) {
                String className = name.replace(CLASS_SUFFIX, "")
                        .replaceAll("/", ".");
                if (isExposeClass(className, type)) {
                    classNames.add(className);
                }
            }
        }
        return classNames;
    }

    /**
     * 判断是否为带有ExposeClass注解的类，并且可指定ExposeClass的type
     * @param className 需要判断的类
     * @param type ExposeClass的type
     * @return
     */
    public static boolean isExposeClass(String className, String type) {
        try {
            Class clazz = Class.forName(className);
            boolean isExpose = isClassWithAnnotation(clazz, ExposeClass.class);
            if (StringUtils.isEmpty(type)) {
                return isExpose;
            }
            if (isExpose) {
                ExposeClass exposeClass = (ExposeClass) clazz.getDeclaredAnnotation(ExposeClass.class);
                isExpose = type.equals(exposeClass.type());
            }
            return isExpose;
        } catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
            logger.error("isExposeClass error: {}, class name: {}, type: {}", e.getMessage(), className, type);
            return false;
        }
    }

}
