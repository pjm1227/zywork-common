package top.zywork.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务状态的枚举<br/>
 * 创建于2019-01-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum SchedulerJobStatusEnum {

    NOT_LANUCH(0, "未启动"),
    RUNNING(1, "运行中"),
    STOPED(2, "已停止"),
    PAUSED(3, "已暂停"),
    DELETED(4, "已删除");

    private Integer value;
    private String des;

    public static Map<Integer, SchedulerJobStatusEnum> lookup;

    static {
        lookup = new HashMap<>();
        for (SchedulerJobStatusEnum goodsOrderStatusEnum : EnumSet.allOf(SchedulerJobStatusEnum.class)) {
            lookup.put(goodsOrderStatusEnum.getValue(), goodsOrderStatusEnum);
        }
    }

    SchedulerJobStatusEnum(Integer value, String des) {
        this.value = value;
        this.des = des;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public static SchedulerJobStatusEnum findByValue(Integer value) {
        return lookup.get(value);
    }

}
