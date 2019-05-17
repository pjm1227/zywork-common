package top.zywork.dos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 默认分销配置类<br/>
 * 创建于2018-12-27<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultDistributionConfig {

    /**
     * 分销等级
     */
    private Integer distributionLevel;
    /**
     * 分销等级反佣比例
     */
    private Map<String, Double> profitPercents;

}
