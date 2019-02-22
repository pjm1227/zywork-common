package top.zywork.vo.goods;

import java.util.Date;

/**
 * 店铺个人认证资料
 * 创建于2019-02-22
 *
 * @author 王振宇
 * @version 1.0
 */
public class GoodsShopPersonalCertificationVO {

    // 身份证号
    private String identity;
    // 真实姓名
    private String realName;
    // 身份证有效期至
    private Date validDate;
    // 身份证正面照片
    private String idcardFront;
    // 身份证反面照片
    private String idcardReverse;
    // 手持身份证照片
    private String idcardHand;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public String getIdcardFront() {
        return idcardFront;
    }

    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront;
    }

    public String getIdcardReverse() {
        return idcardReverse;
    }

    public void setIdcardReverse(String idcardReverse) {
        this.idcardReverse = idcardReverse;
    }

    public String getIdcardHand() {
        return idcardHand;
    }

    public void setIdcardHand(String idcardHand) {
        this.idcardHand = idcardHand;
    }

    @Override
    public String toString() {
        return "GoodsShopPersonalCertificationVO{" +
                "identity='" + identity + '\'' +
                ", realName='" + realName + '\'' +
                ", validDate=" + validDate +
                ", idcardFront='" + idcardFront + '\'' +
                ", idcardReverse='" + idcardReverse + '\'' +
                ", idcardHand='" + idcardHand + '\'' +
                '}';
    }
}
