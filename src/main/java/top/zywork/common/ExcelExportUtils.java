package top.zywork.common;

import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zywork.dto.ExcelExportDTO;
import top.zywork.enums.MIMETypeEnum;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * POI Excel导出工具类，可根据指定的JSON配置文件导出excel<br/>
 * 创建于2017-12-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class ExcelExportUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportUtils.class);
    /**
     * 通过ExcelExportDTO导出数据到excel的Workbook中
     * @param excelExportDTO 由读取JSON配置文件生成的ExcelExportDTO实例
     * @return Workbook工作薄对象
     */
    public static Workbook doExport(ExcelExportDTO excelExportDTO) {
        ExcelUtils excelUtils = new ExcelUtils();
        String fileType = excelExportDTO.getFileType();
        if (fileType == null || "".equals(fileType.trim())) {
            excelExportDTO.setFileType(MIMETypeEnum.XLSX.getExt());
        }
        Workbook workbook = excelUtils.newExcel(excelExportDTO.getFileType());
        Sheet sheet = workbook.createSheet();
        String[] columnNames = excelExportDTO.getColumnNames();
        createTitleRow(excelUtils, sheet, columnNames.length, excelExportDTO.getTitle());
        createHeadRow(excelUtils, sheet, columnNames);
        createContentRows(excelUtils, sheet, excelExportDTO.getProperties(), excelExportDTO.getRows());
        return workbook;
    }

    /**
     * 创建excel标题
     * @param excelUtils ExcelUtils类的实例
     * @param sheet 工作表
     * @param totalColumns 总列数
     * @param title excel标题
     */
    private static void createTitleRow(ExcelUtils excelUtils, Sheet sheet, int totalColumns, String title) {
        Cell cell = excelUtils.createCell(sheet, 0, 0);
        excelUtils.mergeCells(sheet, 0, 0, 0, totalColumns - 1);
        excelUtils.boldCenterStyle(cell);
        cell.setCellValue(title);
    }

    /**
     * 创建excel列头
     * @param excelUtils ExcelUtils类的实例
     * @param sheet 工作表
     * @param columnNames 列名称
     */
    private static void createHeadRow(ExcelUtils excelUtils, Sheet sheet, String[] columnNames) {
        for (int colNo = 0, len = columnNames.length; colNo <  len; colNo++) {
            Cell cell = excelUtils.createCell(sheet, 1, colNo);
            cell.setCellValue(columnNames[colNo]);
            excelUtils.boldStyle(cell);
        }
    }

    /**
     * 创建excel表格内容
     * @param excelUtils ExcelUtils类的实例
     * @param sheet 工作表
     * @param properties 需要显示的内容对应的JavaBean对象中的属性名
     * @param rows 由JavaBean组成的集合
     */
    private static void createContentRows(ExcelUtils excelUtils, Sheet sheet, String[] properties, List<Object> rows) {
        for (int rowNo = 0, totalRows = rows.size(); rowNo < totalRows; rowNo++) {
            Object rowObj = rows.get(rowNo);
            for (int colNo = 0, totalCols = properties.length; colNo < totalCols; colNo++) {
                excelUtils.setCellValue(excelUtils.createCell(sheet, rowNo + 2, colNo),
                        ReflectUtils.invokeGetter(rowObj, properties[colNo]));
            }
        }
    }

    /**
     * 根据JSON导出配置文件的输入流创建ExcelExportDTO对象
     * @param jsonInput json导出配置文件输入流
     * @return json文件对应的ExcelExportDTO
     */
    public static ExcelExportDTO buildExportDTO(InputStream jsonInput) {
        try {
            return JSON.parseObject(jsonInput, ExcelExportDTO.class);
        } catch (IOException e) {
            logger.error("build export dto from input stream error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 根据JSON文本创建ExcelExportDTO对象
     * @param jsonText json文本
     * @return json文本对应的ExcelExportDTO
     */
    public static ExcelExportDTO buildExportDTO(String jsonText) {
        return JSON.parseObject(jsonText, ExcelExportDTO.class);
    }

}
