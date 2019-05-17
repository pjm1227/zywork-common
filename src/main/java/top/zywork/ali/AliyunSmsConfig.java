package top.zywork.ali;

import lombok.*;

/**
 * 阿里云短信配置类<br/>
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
public class AliyunSmsConfig extends AliyunBaseConfig {

    /**
     * 短信签名，必填
     */
    private String signName;

    /**
     * 可用区
     */
    private String regionId;

}
