package top.zywork.ali;

import lombok.*;

/**
 * 阿里云oss配置类<br/>
 * 创建于2019-03-28<br/>
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
public class AliyunOssConfig extends AliyunBaseConfig {

    /**
     * 可用区，如果是内网访问，则添加-internal
     */
    private String regionId;

    /**
     * 绑定的域名
     */
    private String domain;

    /**
     * 默认的bucket
     */
    private String defaultBucket;
}
