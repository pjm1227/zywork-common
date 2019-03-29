package top.zywork.qiniu;

import com.qiniu.common.Zone;

/**
 * 七牛云Zone工具类<br/>
 * 创建于2019-03-28<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class QiniuZoneUtils {

    /**
     * 根据zone名称获取Zone对象
     * @param zoneName
     * @return
     */
    public static Zone getZone(String zoneName) {
        if (QiniuZoneEnum.ZONE0.getZone().equals(zoneName)) {
            return Zone.zone0();
        } else if (QiniuZoneEnum.ZONE1.getZone().equals(zoneName)) {
            return Zone.zone1();
        } else if (QiniuZoneEnum.ZONE2.getZone().equals(zoneName)) {
            return Zone.zone2();
        } else if (QiniuZoneEnum.ZONE_NA0.getZone().equals(zoneName)) {
            return Zone.zoneNa0();
        } else if (QiniuZoneEnum.ZONE_AS0.getZone().equals(zoneName)) {
            return Zone.zoneAs0();
        } else {
            return Zone.autoZone();
        }
    }

}
