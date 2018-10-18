package top.zywork.common.mail;

import top.zywork.common.ConfigUtils;
import top.zywork.common.ExceptionUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件发送器类<br/>
 * 创建于2017-09-14<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
public class MailUtils {

	/**
	 * 邮箱用户名，为邮箱地址
	 */
	public static final String USERNAME = "mail.username";

	/**
	 * 邮箱密码
	 */
	public static final String PASSWORD = "mail.password";

	private static Map<String, Properties> propsMap = new HashMap<>();

	/**
	 * 根据邮件配置信息及邮件对象发送邮件
	 * @param mailConfigPath 邮件配置信息，以classpath:/开头或/WEB-INF开头
	 * @param mail Mail邮件对象
	 */
	public static void sendMail(String mailConfigPath, Mail mail) {
		ConfigUtils configUtils = new ConfigUtils();
		Properties props = propsMap.get(mailConfigPath);
		if (props == null) {
			props = configUtils.build(mailConfigPath);
			propsMap.put(mailConfigPath, props);
		}
		Session session = Session.getInstance(props,
				new MailAuthenticator(configUtils.getString(USERNAME),
						configUtils.getString(PASSWORD)));
		mail.setFrom(new MailAccount(configUtils.getString(USERNAME)));
		try {
			Transport transport = session.getTransport();
			transport.connect();
			Message msg = buildMessage(session, mail);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch (MessagingException e) {
			throw ExceptionUtils.appException(e);
		}
	}

	/**
	 * 由Mail邮件对象创建发送邮件需要的Message对象
	 * @param session 邮件会话对象
	 * @param mail Mail邮件对象
	 * @return Message 邮件消息对象
	 * @throws MessagingException 消息异常
	 */
	private static Message buildMessage(Session session, Mail mail) throws MessagingException {
		Message msg = new MimeMessage(session);
		msg.setFrom(MailAddressUtils.toAddress(mail.getFrom()));
		msg.setSubject(mail.getSubject());
		msg.setContent(mail.getMultipart());
		msg.setRecipients(RecipientType.TO, MailAddressUtils.toAddressArray(mail.getRecipients()));
		if (mail.getCcRecipients() != null) {
			msg.setRecipients(RecipientType.CC, MailAddressUtils.toAddressArray(mail.getCcRecipients()));
		}
		if (mail.getBccRecipients() != null) {
			msg.setRecipients(RecipientType.BCC, MailAddressUtils.toAddressArray(mail.getBccRecipients()));
		}
		return msg;
	}

}


