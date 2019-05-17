package top.zywork.ali;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 阿里云SDK配置基础类<br/>
 * 创建于2018-12-15<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AliyunBaseConfig {

    /**
     * 访问id，必填
     */
    private String accessKeyId;
    /**
     * 访问密钥，必填
     */
    private String accessKeySecret;

}
