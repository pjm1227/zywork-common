package top.zywork.weixin;

/**
 * 存储微信小程序授权获取的session_key与openid的对象<br/>
 *
 * 创建于2018-12-04<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class XcxAuth {

    private String sessionKey;
    private String openid;
    private String unionid;

    public XcxAuth() {}

    public XcxAuth(String sessionKey, String openid) {
        this.sessionKey = sessionKey;
        this.openid = openid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
