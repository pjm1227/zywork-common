package top.zywork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于封装Excel导出的基本信息，此基本信息由json配置文件指定。<br/>
 * rows属性可由外部提供相应的数据源<br/>
 * 创建于2017-12-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelExportDTO extends BaseDTO {

    private static final long serialVersionUID = 5585817536271021363L;

    private String fileType;
    private String title;
    private String[] columnNames;
    private String[] properties;
    private List<Object> rows;

}
