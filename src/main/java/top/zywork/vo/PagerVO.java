package top.zywork.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 返回到前端页面的分页对象
 * 创建于2017-08-16
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagerVO extends BaseVO {

    private static final long serialVersionUID = 7596824634662805852L;

    private Long total;
    private List<Object> rows;

}
