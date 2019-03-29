package top.zywork.qiniu;

/**
 * 七牛zone枚举<br/>
 * 创建于2019-03-28<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum QiniuZoneEnum {
    ZONE0("zone0", "华东"),
    ZONE1("zone1", "华北"),
    ZONE2("zone2", "华南"),
    ZONE_NA0("zoneNa0", "北美"),
    ZONE_AS0("zoneAs0", "东南亚");

    private String zone;
    private String localName;

    QiniuZoneEnum(String zone, String localName) {
        this.zone = zone;
        this.localName = localName;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }
}
