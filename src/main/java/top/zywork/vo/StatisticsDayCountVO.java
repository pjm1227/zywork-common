package top.zywork.vo;

/**
 * 用于统计每日数量的值对象，包含有日期与数量两个值<br/>
 * 创建于2019-01-19<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class StatisticsDayCountVO {

    private String dateStr;
    private Long totalCount;

    public StatisticsDayCountVO() {}

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

}
