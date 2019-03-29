package top.zywork.qiniu;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.enums.CharsetEnum;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 七牛云对象存储工具类<br/>
 * 创建于2019-03-28<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class QiniuOssUtils {

    private static final Logger logger = LoggerFactory.getLogger(QiniuOssUtils.class);

    /**
     * 获取指定bucket的上传凭证
     *
     * @param qiniuOssConfig
     * @param bucketName
     * @return
     */
    public static String getUploadCredential(QiniuOssConfig qiniuOssConfig, String bucketName) {
        Auth auth = Auth.create(qiniuOssConfig.getAccessKey(), qiniuOssConfig.getSecretKey());
        return auth.uploadToken(bucketName);
    }

    /**
     * 获取UploadManager对象
     *
     * @param zoneName
     * @return
     */
    public static UploadManager uploadManager(String zoneName) {
        Configuration configuration = new Configuration(QiniuZoneUtils.getZone(zoneName));
        return new UploadManager(configuration);
    }

    /**
     * 获取BucketManager对象
     *
     * @param qiniuOssConfig
     * @return
     */
    public static BucketManager bucketManager(QiniuOssConfig qiniuOssConfig) {
        Configuration configuration = new Configuration(QiniuZoneUtils.getZone(qiniuOssConfig.getZoneName()));
        Auth auth = Auth.create(qiniuOssConfig.getAccessKey(), qiniuOssConfig.getSecretKey());
        return new BucketManager(auth, configuration);
    }

    /**
     * 上传本地文件到七牛云
     *
     * @param zoneName      如zone0, zone1
     * @param upToken
     * @param key           对象key，可为空，如果为空，则默认使用文件的hash值作为key
     * @param localFilePath 本地文件路径
     * @return
     */
    public static DefaultPutRet putFile(String zoneName, String upToken, String key, String localFilePath) {
        UploadManager uploadManager = uploadManager(zoneName);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            return JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException e) {
            logger.error("put file error: {}", e.response.toString());
            return null;
        }
    }

    /**
     * 上传InputStream
     *
     * @param zoneName
     * @param upToken
     * @param key
     * @param inputStream
     * @return
     */
    public static DefaultPutRet putInputStream(String zoneName, String upToken, String key, InputStream inputStream) {
        UploadManager uploadManager = uploadManager(zoneName);
        try {
            Response response = uploadManager.put(inputStream, key, upToken, null, null);
            return JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException e) {
            logger.error("put inputstream error: {}", e.response.toString());
            return null;
        }
    }

    /**
     * 保存网络资源
     *
     * @param qiniuOssConfig
     * @param bucketName
     * @param key
     * @param url
     * @return
     */
    public static FetchRet putNetworkStream(QiniuOssConfig qiniuOssConfig, String bucketName, String key, String url) {
        BucketManager bucketManager = bucketManager(qiniuOssConfig);
        try {
            return bucketManager.fetch(url, bucketName, key);
        } catch (QiniuException e) {
            logger.error("put network stream error: {}", e.response.toString());
            return null;
        }
    }

    /**
     * 获取bucket内所有对象信息（文件迭代器）
     *
     * @param qiniuOssConfig
     * @param bucketName
     * @return
     */
    public static BucketManager.FileListIterator queryAllObject(QiniuOssConfig qiniuOssConfig, String bucketName) {
        String prefix = "";
        int limit = 1000;
        String delimiter = "";
        BucketManager bucketManager = bucketManager(qiniuOssConfig);
        return bucketManager.createFileListIterator(bucketName, prefix, limit, delimiter);
    }

    /**
     * 删除指定key的对象
     *
     * @param qiniuOssConfig
     * @param bucketName
     * @param key
     */
    public static String deleteObject(QiniuOssConfig qiniuOssConfig, String bucketName, String key) {
        BucketManager bucketManager = bucketManager(qiniuOssConfig);
        try {
            Response response = bucketManager.delete(bucketName, key);
            return response.bodyString();
        } catch (QiniuException e) {
            logger.error("delete object error: {}", e.response.toString());
            return null;
        }
    }

    /**
     * 删除多个key的对象
     *
     * @param qiniuOssConfig
     * @param bucketName
     * @param keys           单次批量删除的数量不能超过1000
     * @return
     */
    public static String deleteObjects(QiniuOssConfig qiniuOssConfig, String bucketName, String[] keys) {
        BucketManager bucketManager = bucketManager(qiniuOssConfig);
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        batchOperations.addDeleteOp(bucketName, keys);
        try {
            Response response = bucketManager.batch(batchOperations);
            return response.bodyString();
        } catch (QiniuException e) {
            logger.error("delete objects error: {}", e.response.toString());
            return null;
        }
    }

    /**
     * 生成对象外网url
     *
     * @param domain 七牛云绑定的域名
     * @param key    对象key
     * @return
     */
    public static String generateObjectUrl(String domain, String key) {
        try {
            return String.format("%s/%s", domain, URLEncoder.encode(key, CharsetEnum.UTF8.getValue()));
        } catch (UnsupportedEncodingException e) {
            logger.error("generateObjectUrl error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 生成私有空间下载链接
     *
     * @param qiniuOssConfig
     * @param key
     * @param expireInSeconds 过期时间，单位为s
     * @return
     */
    public static String generatePresignedUrl(QiniuOssConfig qiniuOssConfig, String key, long expireInSeconds) {
        try {
            String publicUrl = String.format("%s/%s", qiniuOssConfig.getDomain(), URLEncoder.encode(key, CharsetEnum.UTF8.getValue()));
            Auth auth = Auth.create(qiniuOssConfig.getAccessKey(), qiniuOssConfig.getSecretKey());
            return auth.privateDownloadUrl(publicUrl, expireInSeconds);
        } catch (UnsupportedEncodingException e) {
            logger.error("generateObjectUrl error: {}", e.getMessage());
            return null;
        }
    }

}
