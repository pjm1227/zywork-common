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

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

}
