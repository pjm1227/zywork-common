package top.zywork.vo.goods;

/**
 * 店铺公司认证资料，包括企业法人个人信息及公司信息
 * 创建于2019-02-22
 *
 * @author 王振宇
 * @version 1.0
 */
public class GoodsShopCompanyCertificationVO extends GoodsShopPersonalCertificationVO {

    // 公司名
    private String companyName;
    // 信用代码
    private String creditNumber;
    // 营业范围
    private String businessScope;
    // 营业执照
    private String businessLicense;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    @Override
    public String toString() {
        return "GoodsShopCompanyCertificationVO{" +
                "companyName='" + companyName + '\'' +
                ", creditNumber='" + creditNumber + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", businessLicense='" + businessLicense + '\'' +
                '}';
    }
}
