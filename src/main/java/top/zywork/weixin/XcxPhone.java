package top.zywork.weixin;

/**
 * 存储微信小程序获取的手机号信息<br/>
 *
 * 创建于2019-01-07<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class XcxPhone {

    private String phoneNumber;
    private String purePhoneNumber;
    private String countryCode;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPurePhoneNumber() {
        return purePhoneNumber;
    }

    public void setPurePhoneNumber(String purePhoneNumber) {
        this.purePhoneNumber = purePhoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
