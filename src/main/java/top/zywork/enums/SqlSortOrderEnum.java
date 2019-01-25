package top.zywork.enums;

/**
 * 数据库排序枚举<br/>
 *
 * 创建于2019-01-25<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public enum SqlSortOrderEnum {

    ASC("asc"),
    DESC("desc");

    private String value;

    SqlSortOrderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
