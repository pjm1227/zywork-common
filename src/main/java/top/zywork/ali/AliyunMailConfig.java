package top.zywork.ali;

/**
 * 阿里云邮件推送配置类<br/>
 * 创建于2018-12-15<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class AliyunMailConfig extends AliyunBaseConfig {

    /**
     * 可用区
     */
    private String regionId;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

}
