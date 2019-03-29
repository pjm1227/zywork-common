package top.zywork.ali;

/**
 * 阿里云oss配置类<br/>
 * 创建于2019-03-28<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class AliyunOssConfig extends AliyunBaseConfig {

    /**
     * 可用区，如果是内网访问，则添加-internal
     */
    private String regionId;

    /**
     * 绑定的域名
     */
    private String domain;

    /**
     * 默认的bucket
     */
    private String defaultBucket;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
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
