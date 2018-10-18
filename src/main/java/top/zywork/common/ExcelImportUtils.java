package top.zywork.common;

import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.dto.ExcelImportDTO;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * POI Excel导入工具类，可根据指定的JSON配置文件导入excel到内存中<br/>
 * 创建于2017-12-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class ExcelImportUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelImportUtils.class);

    /**
     * 从Excel的Workbook对象中读取数据并导入到内存中
     * @param workbook 工作薄
     * @param excelImportDTO ExcelImportDTO导入对象
     * @return 由工作薄中的表格内容组成的对象集合
     */
    public static List<Object> doImport(Workbook workbook, ExcelImportDTO excelImportDTO) {
        Sheet sheet = workbook.getSheetAt(0);
        ExcelUtils excelUtils = new ExcelUtils();
        String destinationClass = excelImportDTO.getDestinationClass();
        String[] properties = excelImportDTO.getProperties();
        List<Object> rows = new ArrayList<>();
        try {
            Class clazz = Class.forName(destinationClass);
            for (int rowNo = excelImportDTO.getBeginRow(), totalRows = sheet.getLastRowNum(); rowNo <= totalRows; rowNo++) {
                Object rowObj = clazz.newInstance();
                for (int colNo = 0, len = properties.length ; colNo < len; colNo++) {
                    ReflectUtils.invokeSetter(rowObj, properties[colNo],
                            getCellValue(clazz, properties[colNo], excelUtils, sheet, rowNo, colNo));
                }
                rows.add(rowObj);
            }

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            logger.error("import data from workbook error: {}", e.getMessage());
        }
        return rows;
    }

    /**
     * 获取excel工作表中的指定单元和的数据
     * @param clazz 目标Class
     * @param property 属性名
     * @param excelUtils ExcelUtils类的实例
     * @param sheet 工作表
     * @param rowNo 行
     * @param colNo 列
     * @return 指定行与列的单元格的值
     */
    private static Object getCellValue(Class clazz, String property, ExcelUtils excelUtils, Sheet sheet, int rowNo, int colNo) {
        try {
            Field field = clazz.getDeclaredField(property);
            Class type = field.getType();
            if (type == String.class) {
                return excelUtils.getStringCellValueAt(sheet, rowNo, colNo);
            } else if (type == Date.class || type == Timestamp.class) {
                return excelUtils.getDateCellValueAt(sheet, rowNo, colNo);
            } else if (type == Double.class) {
                return excelUtils.getDoubleCellValueAt(sheet, rowNo, colNo);
            } else if (type == Integer.class) {
                return excelUtils.getIntegerCellValueAt(sheet, rowNo, colNo);
            } else if (type == Boolean.class) {
                return excelUtils.getBooleanCellValueAt(sheet, rowNo, colNo);
            } else if (type == Long.class) {
                return excelUtils.getLongCellValueAt(sheet, rowNo, colNo);
            } else if (type == BigDecimal.class) {
                return excelUtils.getBigDecimalCellValueAt(sheet, rowNo, colNo);
            }
        } catch (NoSuchFieldException e) {
            logger.error("read cell value for field error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 根据JSON导入配置文件的输入流创建ExcelImportDTO对象
     * @param jsonInput json导入配置文件输入流
     * @return json导入配置文件对应的ExcelImportDTO实例
     */
    public static ExcelImportDTO buildImportDTO(InputStream jsonInput) {
        try {
            return JSON.parseObject(jsonInput, ExcelImportDTO.class);
        } catch (IOException e) {
            logger.error("build import dto from input stream error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 根据JSON导入配置文本创建ExcelImportDTO对象
     * @param jsonText json导入配置文本
     * @return json文本对应的ExcelImportDTO实例
     */
    public static ExcelImportDTO buildImportDTO(String jsonText) {
        return JSON.parseObject(jsonText, ExcelImportDTO.class);
    }

}
