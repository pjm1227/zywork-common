package top.zywork.ali;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.BatchSendMailRequest;
import com.aliyuncs.dm.model.v20151123.BatchSendMailResponse;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 阿里云邮件推送接口，阿里云邮件推送有三种方式，一是阿里云控制台，二是阿里云邮件api，三是使用smtp<br/>
 * 创建于2018-12-11<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class AliyunMailUtils {

    /**
     * 使用阿里云邮件推送api单个发送邮件
     *
     * @param aliyunMailConfig 阿里云邮件推送配置对象
     * @param accountName      控制台创建的发信地址
     * @param fromAlias        发信人昵称
     * @param toAddress        收件人地址，可以给多个收件人发送邮件，收件人之间用逗号分开
     * @param applyToAddress   是否使用管理控制台中配置的回信地址
     * @param subject          邮件主题
     * @param htmlBody         邮件正文，html格式
     * @param tagName          控制台创建的标签
     * @return
     * @throws ClientException
     */
    public static SingleSendMailResponse sendEmail(AliyunMailConfig aliyunMailConfig, String accountName, String fromAlias, String toAddress, boolean applyToAddress, String subject, String htmlBody, String tagName) throws ClientException {
        IAcsClient client = getClient(aliyunMailConfig);
        SingleSendMailRequest request = new SingleSendMailRequest();
        // 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
        if (!AliyunRegionEnum.CN_HANGZHOU.getRegion().equals(aliyunMailConfig.getRegionId())) {
            request.setVersion("2017-06-22");
        }
        request.setAccountName(accountName);
        request.setFromAlias(fromAlias);
        request.setAddressType(1);
        request.setTagName(tagName);
        request.setReplyToAddress(applyToAddress);
        request.setToAddress(toAddress);
        //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
        //request.setToAddress("邮箱1,邮箱2");
        request.setSubject(subject);
        request.setHtmlBody(htmlBody);
        SingleSendMailResponse httpResponse = client.getAcsResponse(request);
        return httpResponse;
    }

    /**
     * 使用阿里云邮件推送api批量发送模板邮件，需要在阿里云控制台设置好模板，接收人列表
     *
     * @param aliyunMailConfig 阿里云邮件推送配置对象
     * @param accountName      控制台创建的发信地址
     * @param templateName     模板名称
     * @param receiversName    接收列表名称
     * @param tagName          控制台创建的标签
     * @return
     * @throws ClientException
     */
    public static BatchSendMailResponse batchSendEmail(AliyunMailConfig aliyunMailConfig, String accountName, String templateName, String receiversName, String tagName) throws ClientException {
        IAcsClient client = getClient(aliyunMailConfig);
        BatchSendMailRequest request = new BatchSendMailRequest();
        // 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
        if (!AliyunRegionEnum.CN_HANGZHOU.getRegion().equals(aliyunMailConfig.getRegionId())) {
            request.setVersion("2017-06-22");
        }
        request.setAccountName(accountName);
        request.setAddressType(1);
        request.setTemplateName(templateName);
        request.setReceiversName(receiversName);
        request.setTagName(tagName);
        BatchSendMailResponse httpResponse = client.getAcsResponse(request);
        return httpResponse;
    }

    private static IAcsClient getClient(AliyunMailConfig aliyunMailConfig) throws ClientException {
        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
        IClientProfile profile = DefaultProfile.getProfile(aliyunMailConfig.getRegionId(), aliyunMailConfig.getAccessKeyId(), aliyunMailConfig.getAccessKeySecret());
        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
        if (!AliyunRegionEnum.CN_HANGZHOU.getRegion().equals(aliyunMailConfig.getRegionId())) {
            DefaultProfile.addEndpoint("dm." + aliyunMailConfig.getRegionId() + ".aliyuncs.com", aliyunMailConfig.getRegionId(), "Dm", "dm." + aliyunMailConfig.getRegionId() + ".aliyuncs.com");

        }
        return new DefaultAcsClient(profile);
    }

}
