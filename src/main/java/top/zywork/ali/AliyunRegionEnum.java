package top.zywork.ali;

/**
 * 阿里云可用区枚举<br/>
 * 创建于2019-03-28<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum AliyunRegionEnum {

    CN_QINGDAO("cn-qingdao", "青岛"),
    CN_BEIJING("cn-beijing", "北京"),
    CN_ZHANGJIAKOU("cn-zhangjiakou", "张家口"),
    CN_HUHEHAOTE("cn-huhehaote", "呼和浩特"),
    CN_HANGZHOU("cn-hangzhou", "杭州"),
    CN_SHANGHAI("cn-shanghai", "上海"),
    CN_SHENZHEN("cn-shenzhen", "深圳"),
    CN_HONGKONG("cn-hongkong", "香港"),
    AP_SOUTHEAST_1("ap-southeast-1", "新加坡"),
    AP_SOUTHEAST_2("ap-southeast-2", "悉尼"),
    AP_SOUTHEAST_3("ap-southeast-3", "吉隆坡"),
    AP_SOUTHEAST_5("ap-southeast-5", "雅加达"),
    AP_SOUTH_1("ap-south-1", "孟买"),
    AP_NORTHEAST_1("ap-northeast-1", "东京"),
    US_WEST_1("us-west-1", "硅谷"),
    US_EAST_1("us-east-1", "弗吉尼亚"),
    EU_CENTRAL_1("eu-central-1", "法兰克福"),
    EU_WEST_1("eu-west-1", "伦敦"),
    ME_EAST_1("me-east-1", "迪拜");

    private String region;
    private String localName;

    AliyunRegionEnum(String region, String localName) {
        this.region = region;
        this.localName = localName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }
}
