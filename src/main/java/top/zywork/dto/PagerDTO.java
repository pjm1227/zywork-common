package top.zywork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页对象，包括分页需要的参数pageSize和pageNo及分页结果total和rows
 * 创建于2017-08-15
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagerDTO {

    private Long total;
    private List<Object> rows;

}
