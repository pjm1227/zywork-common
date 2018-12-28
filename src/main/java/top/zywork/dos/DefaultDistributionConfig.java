package top.zywork.dos;

import java.util.Map;

/**
 * 默认分销配置类<br/>
 * 创建于2018-12-27<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class DefaultDistributionConfig {

    private Integer distributionLevel;
    private Map<String, Double> profitPercents;

    public DefaultDistributionConfig() {}

    public DefaultDistributionConfig(Integer distributionLevel, Map<String, Double> profitPercents) {
        this.distributionLevel = distributionLevel;
        this.profitPercents = profitPercents;
    }

    public Integer getDistributionLevel() {
        return distributionLevel;
    }

    public void setDistributionLevel(Integer distributionLevel) {
        this.distributionLevel = distributionLevel;
    }

    public Map<String, Double> getProfitPercents() {
        return profitPercents;
    }

    public void setProfitPercents(Map<String, Double> profitPercents) {
        this.profitPercents = profitPercents;
    }
}
