package top.zywork.enums;

/**
 * 用户提现状态的枚举<br/>
 * 创建于2018-12-23<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum WithdrawStatusEnum {


    CHECKING(0, "审核中"),
    CHECKED(1, "已通过"),
    UNCHECKED(2, "未通过"),
    SUCCESS(3, "成功"),
    FAILURE(4, "失败");

    private Integer value;
    private String des;

    WithdrawStatusEnum(Integer value, String des) {
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
}
