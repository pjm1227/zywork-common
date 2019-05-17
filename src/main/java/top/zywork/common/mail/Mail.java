package top.zywork.common.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 邮件对象类，封装邮件属性，如发送者，接收者，主题，内容，类型等<br/>
 * 创建于2017-09-14<br/>
 *
 * @author 王振宇
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class Mail {

	/**
	 * 邮件发送者
	 */
	private MailAccount from;
	/**
	 * 邮件接收者
	 */
	private List<MailAccount> recipients;
	/**
	 * 抄送人
	 */
	private List<MailAccount> ccRecipients;
	/**
	 * 密送人
	 */
	private List<MailAccount> bccRecipients;
	/**
	 * 邮件主题
	 */
	private String subject;
	/**
	 * 邮件内容
	 */
	private String content;
	/**
	 * 内容类型
	 */
	private String contentType;
	/**
	 * 邮件附件
	 */
	private List<String> files;

	public Multipart getMultipart() {
		Multipart multipart = new MimeMultipart();
		try {
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setContent(content, contentType);
			multipart.addBodyPart(contentPart);
			for (String path : files) {
				BodyPart attachmentPart = new MimeBodyPart();
				File file = new File(path);
				attachmentPart.setDataHandler(new DataHandler(new FileDataSource(file)));
				attachmentPart.setFileName(MimeUtility.encodeText(file.getName()));
				multipart.addBodyPart(attachmentPart);
			}
		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error("get multipart error: {}", e.getMessage());
		}
		return multipart;
	}

}
