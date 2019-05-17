package top.zywork.common.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮箱账户类，包括邮箱地址和邮箱的昵称<br/>
 * 创建于2017-09-14<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailAccount {

    /**
     * 邮件地址
     */
    private String address;
    /**
     * 昵称
     */
    private String personal;

}
