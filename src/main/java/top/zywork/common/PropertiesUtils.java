package top.zywork.common;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

/**
 * 配置工具类，由此类读取指定路径的配置文件
 * 创建于2017-08-15
 *
 * @author WangZhenyu
 * @version 1.0
 */
@Slf4j
public class PropertiesUtils {

    private Properties properties;

    public PropertiesUtils() {}

    /**
     * 根据指定的配置文件路径读取文件内容到Properties实例中
     * @param location 配置文件的路径，包含两种形式，classpath:/和/WEB-INF的两个位置
     * @return Properties对象
     * @see FileUtils
     */
    public Properties read(String location) {
        properties = new Properties();
        try (
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                new BufferedInputStream(
                                        new FileInputStream(FileUtils.getResourcePath(location)))))
        ) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            log.error("read config file from {} error: {}", location, e.getMessage());
        }
        return properties;
    }

    /**
     * 通过指定的key值去获取properties文件中整数值
     * @param key properties文件中的key
     * @return 返回key值对应的整数值，当且仅当此值是一个整数类型值时
     */
    public Integer getInteger(String key) {
        String value = properties.getProperty(key);
        return value == null ? null : Integer.valueOf(value);
    }

    /**
     * 通过指定的key值去获取properties文件中字符串值
     * @param key properties文件中的key
     * @return 返回key值对应的字符串值
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * 通过指定的key值去获取properties文件中double值
     * @param key properties文件中的key
     * @return 返回key值对应的double值，当且仅当此值是一个数值类型值时
     */
    public Double getDouble(String key) {
        String value = properties.getProperty(key);
        return value == null ? null : Double.valueOf(value);
    }

    /**
     * 通过指定的key值去获取properties文件中长整型值
     * @param key properties文件中的key
     * @return 返回key值对应的长整型值，当且仅当此值是一个整数类型值时
     */
    public Long getLong(String key) {
        String value = properties.getProperty(key);
        return value == null ? null : Long.valueOf(value);
    }

    public Boolean getBoolean(String key) {
        String value = properties.getProperty(key);
        return value == null ? null : Boolean.valueOf(value);
    }

}
