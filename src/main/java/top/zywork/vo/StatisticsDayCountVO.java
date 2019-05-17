package top.zywork.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于统计每日数量的值对象，包含有日期与数量两个值<br/>
 * 创建于2019-01-19<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsDayCountVO {

    /**
     * 日期
     */
    private String dateStr;

    /**
     * 总数
     */
    private Long totalCount;

}
