package top.zywork.query;

/**
 * 分页查询对象<br/>
 * 创建于2017-08-23<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class PageQuery extends BaseQuery {

    private static final long serialVersionUID = 2691743450470042585L;

    private static final int DEFAULT_PAGE_SIZE = 10;

    // 第几页
    private Integer pageNo;
    // 每页多少项
    private Integer pageSize;
    // 排序字段
    private String sortColumn;
    // 排序规则
    private String sortOrder;

    public PageQuery(){}

    public PageQuery(Integer pageNo, Integer pageSize, String sortColumn, String sortOrder) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.sortColumn = sortColumn;
        this.sortOrder = sortOrder;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo > 0 ? pageNo : 1;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
    }

    public int getBeginIndex() {
        return (pageNo - 1) * pageSize;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
