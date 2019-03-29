package top.zywork.qiniu;

/**
 * 七牛云对象存储配置基础类<br/>
 * 创建于2019-03-28<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class QiniuOssConfig extends QiniuBaseConfig {

    private String zoneName;
    private String domain;
    /**
     * 默认的bucket
     */
    private String defaultBucket;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDefaultBucket() {
        return defaultBucket;
    }

    public void setDefaultBucket(String defaultBucket) {
        this.defaultBucket = defaultBucket;
    }
}
