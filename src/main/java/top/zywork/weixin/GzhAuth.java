package top.zywork.weixin;

/**
 * 存储微信公众号网页授权登录获取的access_token与openid的对象<br/>
 *
 * 创建于2018-12-04<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class GzhAuth {

    private String accessToken;
    private String openid;

    public GzhAuth() {}

    public GzhAuth(String accessToken, String openid) {
        this.accessToken = accessToken;
        this.openid = openid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
