package org.springblade.common.utils;

import org.springblade.common.component.AbstractMailSender;
import org.springblade.common.enums.EmailType;
import org.springblade.core.log.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author: DestinyStone
 * @Date: 2022/3/16 12:46
 * @Description:
 */
@Component
public class EmailUtils {
	private static AbstractMailSender[] mailSender;

	public static void send(String subject,  String content, String to, EmailType type) throws MessagingException {
		AbstractMailSender currentMailSender = getMailSender(type);
		if (currentMailSender == null) {
			throw new ServiceException("不支持的邮件类型");
		}
		currentMailSender.reset();
		MimeMessage mimeMessage = currentMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		helper.setSubject(subject);
		helper.setTo(to);
		helper.setFrom(type.getFrom());
		helper.setText(content, true);
		currentMailSender.send(mimeMessage);
	}

	private static AbstractMailSender getMailSender(EmailType type) {
		for (AbstractMailSender abstractMailSender : mailSender) {
			if (type.equals(abstractMailSender.getType())) {
				return abstractMailSender;
			}
		}
		return null;
	}

	@Autowired
	public void setMailSender(AbstractMailSender[] mailSender) {
		EmailUtils.mailSender = mailSender;
	}
}
