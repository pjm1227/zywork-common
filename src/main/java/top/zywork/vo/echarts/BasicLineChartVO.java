package top.zywork.vo.echarts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * echarts basic line chart<br/>
 * 创建于2019-01-19<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicLineChartVO {

    private List<String> xAxisData;
    private List<Number> seriesData;

    public void setxAxisData(List<String> xAxisData) {
        this.xAxisData = xAxisData;
    }

    public List<String> getxAxisData() {
        return xAxisData;
    }

}
