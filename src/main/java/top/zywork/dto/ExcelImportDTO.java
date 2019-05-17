package top.zywork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于封装Excel导入的基本信息，此基本信息由json配置文件指定。<br/>
 * rows属性为需要导入的数据<br/>
 * 创建于2017-12-18<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelImportDTO extends BaseDTO{

    private static final long serialVersionUID = 7358466249603435234L;

    private String fileType;
    private Integer beginRow;
    private String[] properties;
    private String destinationClass;

}
