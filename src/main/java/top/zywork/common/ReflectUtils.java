package top.zywork.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import top.zywork.annotation.ExposeClass;
import top.zywork.constant.FileConstants;

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
@Slf4j
public class ReflectUtils {

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
            log.error("reflect invoke get method error: {}", e.getMessage());
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
                log.error("reflect invoke set method error: {}", e.getMessage());
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
            log.error("reflect get property value error: {}", e.getMessage());
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
            log.error("reflect set property value error: {}", e.getMessage());
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
            return getArgsNames(method);
        } catch (NoSuchMethodException e) {
            log.error("getArgsNames error: {}", e.getMessage());
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
               return getArgsNames(method);
            }
        }
        return null;
    }

    /**
     * 通过方法名获取参数名列表
     * @param method
     * @return
     */
    private static String[] getArgsNames(Method method) {
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
        packageName = packageName.replace(".", FileConstants.SEPARATOR);
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
            log.error("getClassNames error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取指定包中的所有带有指定注解的类对象，如top.zywork.common.Test
     * @param packageName 指定包名
     * @param recursive 是否迭代查询包
     * @param annotation 如果指定了Annotation，则只会去获取带有此Annotation的类信息，如果未指定，为null，则获取所有的类信息
     * @return
     */
    public static List<Class<?>> getClasses(String packageName, Boolean recursive, Class<? extends Annotation> annotation) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        packageName = packageName.replace(".", FileConstants.SEPARATOR);
        List<Class<?>> classes = new ArrayList<>();
        try {
            Enumeration<URL> urlEnumeration = classLoader.getResources(packageName);
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    classes.addAll(getClassesFromFile(new File(url.getPath()), recursive, annotation));
                } else if ("jar".equals(protocol)) {
                    classes.addAll(getClassesFromJar(((JarURLConnection) url.openConnection()).getJarFile(), annotation));
                }
            }
            return classes;
        } catch (IOException e) {
            log.error("getClasses error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 以文件的方式获取类名称
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
                                .replace(File.separator, ".");
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
     * 以文件的方式获取类对象
     * @param file
     * @param recursive
     * @param annotation
     * @return
     */
    private static List<Class<?>> getClassesFromFile(File file, Boolean recursive, Class<? extends Annotation> annotation) {
        List<Class<?>> classes = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (!f.isDirectory()) {
                    String classPath = f.getPath();
                    // 过滤掉所有内部类
                    if (classPath.endsWith(CLASS_SUFFIX) && !isInnerClass(classPath)) {
                        String className = classPath.substring(classPath.indexOf(CLASS_PATH_PREFIX) + CLASS_PATH_PREFIX.length())
                                .replace(CLASS_SUFFIX, "")
                                .replace(File.separator, ".");
                        addToClassesList(classes, className, annotation);
                    }
                } else {
                    if (recursive) {
                        classes.addAll(getClassesFromFile(f, recursive, annotation));
                    }
                }
            }
        }
        return classes;
    }

    /**
     * 通过JarFile获取类名称
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
                        .replace(File.separator, ".");
                if (isClassWithAnnotation(className, annotation)) {
                    classNames.add(className);
                }
            }
        }
        return classNames;
    }

    /**
     * 通过JarFile获取类对象
     * @param jarFile
     * @param annotation
     * @return
     */
    private static List<Class<?>> getClassesFromJar(JarFile jarFile, Class<? extends Annotation> annotation) {
        List<Class<?>> classes = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 过滤掉所有内部类
            if (name.endsWith(CLASS_SUFFIX) && !isInnerClass(name)) {
                String className = name.replace(CLASS_SUFFIX, "")
                        .replace(File.separator, ".");
                addToClassesList(classes, className, annotation);
            }
        }
        return classes;
    }

    /**
     * 把符合条件的类添加到 List 集合中
     */
    private static void addToClassesList(List<Class<?>> classes, String className, Class<? extends Annotation> annotation) {
        try {
            Class<?> clazz = Class.forName(className);
            if (isClassWithAnnotation(clazz, annotation)) {
                classes.add(clazz);
            }
        } catch (ClassNotFoundException e) {
            log.error("load class from classname: {} error", className);
        }
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
            log.error("isClassWithAnnotation error: {}, class name: {}", e.getMessage(), className);
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
        List<Class<?>> classes = getClasses(packageName, recursive, ExposeClass.class);
        List<String> classNames = new ArrayList<>();
        if (classes != null && classes.size() > 0) {
            if (StringUtils.isEmpty(type)) {
                for (Class<?> clazz : classes) {
                    classNames.add(clazz.getName());
                }
            } else {
                for (Class<?> clazz : classes) {
                    ExposeClass exposeClass = clazz.getDeclaredAnnotation(ExposeClass.class);
                    if (type.equals(exposeClass.type())) {
                        classNames.add(clazz.getName());
                    }
                }
            }
        }
        return classNames;
    }

}
