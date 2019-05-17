package top.zywork.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.zywork.enums.ResponseStatusEnum;

/**
 * 控制器结果，通常都是在页面上执行添加，删除，状态改变等操作时，由控制器返回执行结果的JSON格式数据到前端页面<br/>
 * 创建于2017-08-16<br/>
 *
 * @author 王振宇
 * @version 1.0
 * @see ResponseStatusEnum
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseStatusVO extends BaseVO {

    private static final long serialVersionUID = 4997020566681368159L;

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回消息
     */
    private String message;
    /**
     * 返回数据
     */
    private Object data;

    /**
     * 获取表示执行成功的结果
     *
     * @param code 状态码
     * @param message 需要返回到前端页面的提示信息
     * @param data 返回数据
     * @return
     */
    public static ResponseStatusVO ok(Integer code, String message, Object data) {
        return new ResponseStatusVO(code, message, data);
    }

    /**
     * 获取表示执行成功的结果
     *
     * @param message 需要返回到前端页面的提示信息
     * @param data 返回数据
     * @return
     */
    public static ResponseStatusVO ok(String message, Object data) {
        return new ResponseStatusVO(ResponseStatusEnum.OK.getCode(), message, data);
    }

    /**
     * 获取表示执行失败的结果
     *
     * @param code 状态码
     * @param message 需要返回到前端页面的提示信息
     * @param data 返回数据
     * @return
     */
    public static ResponseStatusVO error(Integer code, String message, Object data) {
        return new ResponseStatusVO(code, message, data);
    }

    /**
     * 获取表示执行失败的结果
     *
     * @param message 需要返回到前端页面的提示信息
     * @param data 返回数据
     * @return
     */
    public static ResponseStatusVO error(String message, Object data) {
        return new ResponseStatusVO(ResponseStatusEnum.ERROR.getCode(), message, data);
    }

    /**
     * 获取表示数据错误的结果
     *
     * @param code 状态码
     * @param message 需要返回到前端页面的提示信息
     * @param data 返回数据
     * @return
     */
    public static ResponseStatusVO dataError(Integer code, String message, Object data) {
        return new ResponseStatusVO(code, message, data);
    }

    /**
     * 获取表示数据错误的结果
     *
     * @param message 需要返回到前端页面的提示信息
     * @param data 返回数据
     * @return
     */
    public static ResponseStatusVO dataError(String message, Object data) {
        return new ResponseStatusVO(ResponseStatusEnum.DATA_ERROR.getCode(), message, data);
    }

    /**
     * 获取未认证的用户结果
     * @return
     */
    public static ResponseStatusVO authenticationError() {
        return new ResponseStatusVO(ResponseStatusEnum.AUTHENTICATION_ERROR.getCode(), ResponseStatusEnum.AUTHENTICATION_ERROR.getMessage(), null);
    }

    /**
     * 获取未授权的用户结果
     * @return
     */
    public static ResponseStatusVO authorizationError() {
        return new ResponseStatusVO(ResponseStatusEnum.AUTHORIZATION_ERROR.getCode(), ResponseStatusEnum.AUTHORIZATION_ERROR.getMessage(), null);
    }

}
