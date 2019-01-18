package top.zywork.common;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 使用RedisTemplate的Redis工具类<br/>
 *
 * 创建于2019-01-17<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class RedisUtils {

    public static final String KEY_LOCK_PREFIX = "key_lock::";

    /**
     * 存储
     * @param redisTemplate
     * @param key
     * @param value
     */
    public static void save(RedisTemplate<String, Object> redisTemplate, String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 存储
     * @param redisTemplate
     * @param key
     * @param value
     * @param expiration
     * @param timeUnit
     */
    public static void save(RedisTemplate<String, Object> redisTemplate, String key, Object value, long expiration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expiration, timeUnit);
    }

    /**
     * 判断key是否存在
     * @param redisTemplate
     * @param key
     * @return
     */
    public static Boolean exists(RedisTemplate<String, Object> redisTemplate, String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取token
     * @param redisTemplate
     * @param key
     * @return
     */
    public static Object get(RedisTemplate<String, Object> redisTemplate, String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除指定的key
     * @param redisTemplate
     * @param key
     */
    public static Boolean delete(RedisTemplate<String, Object> redisTemplate, String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除指定的keys
     * @param redisTemplate
     * @param keys
     * @return
     */
    public static Long deleteKeys(RedisTemplate<String, Object> redisTemplate, List<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 获取失效时间
     * @param redisTemplate
     * @param key
     * @param timeUnit
     * @return
     */
    public static Long getExpiration(RedisTemplate<String, Object> redisTemplate, String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 刷新失效时间
     * @param redisTemplate
     * @param key
     * @param expiration
     * @param timeUnit
     */
    public static Boolean refreshExpiration(RedisTemplate<String, Object> redisTemplate, String key, long expiration, TimeUnit timeUnit) {
        return redisTemplate.expire(key, expiration, timeUnit);
    }

    /**
     * 给指定的key加锁
     * @param redisTemplate
     * @param key
     * @param expiration
     * @param timeUnit
     * @return 返回false表示通信失败或已经加锁，返回true表示加锁成功
     */
    public static boolean lock(RedisTemplate<String, Object> redisTemplate, String key, long expiration, TimeUnit timeUnit) {
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(KEY_LOCK_PREFIX + key, 1, expiration, timeUnit);
        return locked == null ? false : locked;
    }

    /**
     * 给指定的key解锁
     * @param redisTemplate
     * @param key
     * @return 返回false表示通信失败或解锁失败，返回true表示解锁成功
     */
    public static boolean unlock(RedisTemplate<String, Object> redisTemplate, String key) {
        Boolean unlocked = redisTemplate.delete(KEY_LOCK_PREFIX + key);
        return unlocked == null ? false : unlocked;
    }

}
