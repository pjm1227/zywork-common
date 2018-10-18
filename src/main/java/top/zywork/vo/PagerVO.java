package top.zywork.vo;

import java.util.List;

/**
 * 返回到前端页面的分页对象
 * 创建于2017-08-16
 *
 * @author 王振宇
 * @version 1.0
 */
public class PagerVO extends BaseVO {

    private static final long serialVersionUID = 7596824634662805852L;

    private Long total;
    private List<Object> rows;

    public PagerVO() {}

    public PagerVO(Long total, List<Object> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Object> getRows() {
        return rows;
    }

    public void setRows(List<Object> rows) {
        this.rows = rows;
    }


    @Override
    public String toString() {
        return "PagerVO{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
