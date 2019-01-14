package top.zywork.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.enums.DatePatternEnum;

import java.math.BigDecimal;

/**
 * JDBC元数据工具类<br/>
 *
 * 创建于2018-03-12<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class JDBCUtils {

    private static final Logger logger = LoggerFactory.getLogger(JDBCUtils.class);

    /**
     * 获取字段对应的Java类型的名称
     * @param jdbcType
     * @return
     */
    public String getJavaType(String jdbcType) {
        switch(jdbcType){
            case "VARCHAR":
            case "VARCHAR2":
            case "CHAR":
            case "TEXT":
                return "String";
            case "NUMBER":
            case "DECIMAL":
                return "BigDecimal";
            case "INT":
            case "INTEGER":
                return "Integer";
            case "SMALLINT":
                return "Short";
            case "TINYINT":
                return "Byte";
            case "BIGINT":
                return "Long";
            case "DOUBLE":
                return "Double";
            case "FLOAT":
                return "Float";
            case "DATETIME":
            case "TIMESTAMP":
            case "DATE":
            case "TIME":
            case "YEAR":
                return "Date";
            default:
                return "String";
        }
    }

    /**
     * 根据jdbc type把字符中的值转化为与jdbc type对应的Java类型的值
     * @param value
     * @param jdbcType
     * @return
     */
    public Object convert(String value, String jdbcType) {
        switch(jdbcType){
            case "VARCHAR":
            case "VARCHAR2":
            case "CHAR":
            case "TEXT":
                return value;
            case "NUMBER":
            case "DECIMAL":
                return new BigDecimal(value);
            case "INT":
            case "INTEGER":
                return Integer.valueOf(value);
            case "SMALLINT":
                return Short.valueOf(value);
            case "TINYINT":
                return Byte.valueOf(value);
            case "BIGINT":
                return Long.valueOf(value);
            case "DOUBLE":
                return Double.valueOf(value);
            case "FLOAT":
                return Float.valueOf(value);
            case "DATETIME":
            case "TIMESTAMP":
            case "DATE":
            case "TIME":
            case "YEAR":
                return DateParseUtils.parseDate(value, DatePatternEnum.DATETIME.getValue());
            default:
                return value;
        }
    }

}
