package top.zywork.vo;

import top.zywork.enums.ResponseStatusEnum;

/**
 * 控制器结果，通常都是在页面上执行添加，删除，状态改变等操作时，由控制器返回执行结果的JSON格式数据到前端页面<br/>
 * 创建于2017-08-16<br/>
 *
 * @author 王振宇
 * @version 1.0
 * @see ResponseStatusEnum
 */
public class ResponseStatusVO extends BaseVO {

    private static final long serialVersionUID = 4997020566681368159L;

    private static final String STATUS_OK = "ok";
    private static final String STATUS_ERROR = "error";
    private static final String STATUS_DATA_ERROR = "data-error";

    // 状态码
    private Integer code;
    // 返回消息
    private String message;
    // 返回数据
    private Object data;

    public ResponseStatusVO() {}

    public ResponseStatusVO(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 获取表示执行成功的结果
     *
     * @param code 状态码
     * @param message 需要返回到前端页面的提示信息
     */
    public void okStatus(Integer code, String message, Object data) {
        setCode(code);
        setMessage(message);
        setData(data);
    }

    /**
     * 获取表示执行失败的结果
     *
     * @param code 状态码
     * @param message 需要返回到前端页面的提示信息
     */
    public void errorStatus(Integer code, String message, Object data) {
        setCode(code);
        setMessage(message);
        setData(data);
    }

    /**
     * 获取表示数据错误的结果
     *
     * @param code 状态码
     * @param message 需要返回到前端页面的提示信息
     * @return 表示数据错误的结果
     */
    public void dataErrorStatus(Integer code, String message, Object data) {
        setCode(code);
        setMessage(message);
        setData(data);
    }

    @Override
    public String toString() {
        return "ResponseStatusVO{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
