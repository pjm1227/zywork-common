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
}
