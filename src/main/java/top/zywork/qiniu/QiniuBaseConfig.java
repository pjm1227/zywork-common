package top.zywork.qiniu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 七牛sdk配置基础类<br/>
 * 创建于2019-03-28<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QiniuBaseConfig {

    /**
     * key
     */
    private String accessKey;
    /**
     * secret
     */
    private String secretKey;

}
