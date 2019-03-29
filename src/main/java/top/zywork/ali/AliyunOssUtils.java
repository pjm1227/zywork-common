package top.zywork.ali;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.common.DateUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 阿里云oss工具类<br/>
 * 创建于2019-03-28<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class AliyunOssUtils {

    private static final Logger logger = LoggerFactory.getLogger(AliyunOssUtils.class);

    private static OSSClient ossClient;

    /**
     * 单例模式获取OSSClient
     *
     * @param aliyunOssConfig
     * @return
     */
    public static OSSClient getClientInstance(AliyunOssConfig aliyunOssConfig) {
        if (ossClient == null) {
            synchronized (AliyunOssUtils.class) {
                if (ossClient == null) {
                    ossClient = getClient(aliyunOssConfig);
                }
            }
        }
        return ossClient;
    }

    /**
     * 非单例模式获取OSSClient
     *
     * @param aliyunOssConfig
     * @return
     */
    public static OSSClient getClient(AliyunOssConfig aliyunOssConfig) {
        return new OSSClient("http://oss-" + aliyunOssConfig.getRegionId() + ".aliyuncs.com", aliyunOssConfig.getAccessKeyId(), aliyunOssConfig.getAccessKeySecret());
    }

    /**
     * 关闭OSSClient
     *
     * @param ossClient
     */
    public static void closeClient(OSSClient ossClient) {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    /**
     * 创建bucket
     *
     * @param ossClient
     * @param bucketName
     */
    public static Bucket createBucket(OSSClient ossClient, String bucketName) {
        if (ossClient.doesBucketExist(bucketName)) {
            return ossClient.getBucketInfo(bucketName).getBucket();
        } else {
            return ossClient.createBucket(bucketName);
        }
    }

    /**
     * 删除bucket
     *
     * @param ossClient
     * @param bucketName
     */
    public static void deleteBucket(OSSClient ossClient, String bucketName) {
        ossClient.deleteBucket(bucketName);
    }

    /**
     * 保存File
     *
     * @param ossClient
     * @param bucketName
     * @param objectKey  objectKey可以是目录形式，如images/goods
     * @param file
     * @return
     */
    public static PutObjectResult putFile(OSSClient ossClient, String bucketName, String objectKey, File file) {
        return ossClient.putObject(bucketName, objectKey, file);
    }

    /**
     * 保存InputStream
     *
     * @param ossClient
     * @param bucketName
     * @param objectKey   objectKey可以是目录形式，如images/goods
     * @param inputStream
     * @return
     */
    public static PutObjectResult putInputStream(OSSClient ossClient, String bucketName, String objectKey, InputStream inputStream) {
        return ossClient.putObject(bucketName, objectKey, inputStream);
    }

    /**
     * 根据url地址保存InputStream
     *
     * @param ossClient
     * @param bucketName
     * @param objectKey
     * @param url
     * @return
     */
    public static PutObjectResult putNetworkStream(OSSClient ossClient, String bucketName, String objectKey, String url) {
        try {
            return ossClient.putObject(bucketName, objectKey, new URL(url).openStream());
        } catch (IOException e) {
            logger.error("error open url: {}", url);
            return null;
        }
    }

    /**
     * 根据key获取InputStream
     *
     * @param ossClient
     * @param bucketName
     * @param objectKey
     * @return
     */
    public static InputStream getInputStreamFromOss(OSSClient ossClient, String bucketName, String objectKey) {
        InputStream inputStream = null;
        try {
            OSSObject ossObj = ossClient.getObject(bucketName, objectKey);
            inputStream = ossObj.getObjectContent();
            ossObj.close();
        } catch (OSSException | IOException e) {
            logger.error("getInputStreamFromOss error, objectKey: {}", objectKey);
        }
        return inputStream;
    }

    /**
     * 获取对象元数据
     *
     * @param ossClient
     * @param bucketName
     * @param objectKey
     */
    public static ObjectMetadata getObjectMetadata(OSSClient ossClient, String bucketName, String objectKey) {
        return ossClient.getObjectMetadata(bucketName, objectKey);
    }

    /**
     * 获取对象content type
     *
     * @param ossClient
     * @param bucketName
     * @param objectKey
     * @return
     */
    public static String getContentType(OSSClient ossClient, String bucketName, String objectKey) {
        return getObjectMetadata(ossClient, bucketName, objectKey).getContentType();
    }

    /**
     * 查询指定bucket中的所有对象的相关信息，包括key和size
     *
     * @param ossClient
     * @param bucketName
     * @return
     */
    public static List<String> queryAllObject(OSSClient ossClient, String bucketName) {
        List<String> results = new ArrayList<>();
        ObjectListing objectListing = ossClient.listObjects(bucketName);
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            results.add(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
        }
        return results;
    }

    /**
     * 删除指定key的对象
     *
     * @param ossClient
     * @param bucketName
     * @param objectKey
     */
    public static void deleteObject(OSSClient ossClient, String bucketName, String objectKey) {
        ossClient.deleteObject(bucketName, objectKey);
    }

    /**
     * 删除多个key对象
     *
     * @param ossClient
     * @param bucketName
     * @param objectKeys
     * @return
     */
    public static DeleteObjectsResult deleteObjects(OSSClient ossClient, String bucketName, List<String> objectKeys) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        deleteObjectsRequest.setKeys(objectKeys);
        return ossClient.deleteObjects(deleteObjectsRequest);
    }

    /**
     * 生成外网访问url
     *
     * @param regionId
     * @param bucketName
     * @param objectKey
     * @return
     */
    public static String generateObjectUrl(String regionId, String bucketName, String objectKey) {
        return "https://" + bucketName + ".oss-" + regionId + ".aliyuncs.com/" + objectKey;
    }

    /**
     * 生成内网访问url
     *
     * @param regionId
     * @param bucketName
     * @param objectKey
     * @return
     */
    public static String generateObjectInternalUrl(String regionId, String bucketName, String objectKey) {
        return "https://" + bucketName + ".oss-" + regionId + "-internal.aliyuncs.com/" + objectKey;
    }

    /**
     * 使用绑定的域名生成外网访问链接
     *
     * @param domain
     * @param objectKey
     * @return
     */
    public static String generateObjectUrl(String domain, String objectKey) {
        return domain + "/" + objectKey;
    }

    /**
     * 生成带有授权信息的链接
     *
     * @param ossClient
     * @param bucketName
     * @param objectKey
     * @param expireInMillis 过期时间，单位为ms
     * @return
     */
    public static String generatePresignedUrl(OSSClient ossClient, String bucketName, String objectKey, long expireInMillis) {
        return ossClient.generatePresignedUrl(bucketName, objectKey, DateUtils.millisToDate(DateUtils.currentTimeMillis() + expireInMillis)).toString();
    }

}
