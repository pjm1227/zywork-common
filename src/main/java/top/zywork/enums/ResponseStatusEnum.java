package top.zywork.enums;

/**
 * 响应状态枚举<br/>
 * 创建于2017-08-16<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum ResponseStatusEnum {

    OK(1001, "成功"),
    ERROR(1002, "系统错误"),
    DATA_ERROR(1003, "参数错误"),
    AUTHENTICATION_ERROR(1004, "未认证的用户"),
    AUTHORIZATION_ERROR(1005, "未授权的用户");

    private Integer code;
    private String message;

    ResponseStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
