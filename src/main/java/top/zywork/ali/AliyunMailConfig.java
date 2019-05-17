package top.zywork.ali;

import lombok.*;

/**
 * 阿里云邮件推送配置类<br/>
 * 创建于2018-12-15<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AliyunMailConfig extends AliyunBaseConfig {

    /**
     * 可用区
     */
    private String regionId;

}
