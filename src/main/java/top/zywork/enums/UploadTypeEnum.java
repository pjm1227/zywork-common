package top.zywork.enums;

/**
 * 文件上传类型枚举，定义可上传的后缀及文件大小限制<br/>
 * 创建于2019-01-02<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum UploadTypeEnum {

    IMAGE(".jpg,.jpeg,.png,.gif,.bmp", 10 * 1024 * 1024L),
    OFFICE(".doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.txt", 10 * 1024 * 1024L),
    ZIP(".zip,.rar", 10 * 1024 * 1024L),
    OFFICE_ZIP(".doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.txt,.zip,.rar", 10 * 1024 * 1024L);

    private String allowedExts;
    private Long maxSize;

    UploadTypeEnum(String allowedExts, Long maxSize) {
        this.allowedExts = allowedExts;
        this.maxSize = maxSize;
    }

    public String getAllowedExts() {
        return allowedExts;
    }

    public void setAllowedExts(String allowedExts) {
        this.allowedExts = allowedExts;
    }

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }
}
