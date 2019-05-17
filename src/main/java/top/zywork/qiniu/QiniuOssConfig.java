package top.zywork.qiniu;

import lombok.*;

/**
 * 七牛云对象存储配置基础类<br/>
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
public class QiniuOssConfig extends QiniuBaseConfig {

    private String zoneName;
    private String domain;
    /**
     * 默认的bucket
     */
    private String defaultBucket;

}
