package top.zywork.vo.echarts;

import java.util.List;

/**
 * echarts basic line chart<br/>
 * 创建于2019-01-19<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class BasicLineChartVO {

    private List<String> xAxisData;
    private List<Number> seriesData;

    public BasicLineChartVO() {}

    public List<String> getxAxisData() {
        return xAxisData;
    }

    public void setxAxisData(List<String> xAxisData) {
        this.xAxisData = xAxisData;
    }

    public List<Number> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<Number> seriesData) {
        this.seriesData = seriesData;
    }

}
