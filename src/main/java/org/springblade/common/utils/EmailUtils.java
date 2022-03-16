package org.springblade.common.utils;

import org.springblade.common.enums.EmailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
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
	private static JavaMailSender mailsender;

	public static void send(String subject,  String content, String to, EmailType type) throws MessagingException {
		if (EmailType.QQ.equals(type)) {
			MimeMessage mimeMessage = mailsender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setSubject(subject);
			helper.setTo(to);
			helper.setFrom(type.getFrom());
			helper.setText(content, true);
			mailsender.send(mimeMessage);
		}
	}

	@Autowired
	public void setMailsender(JavaMailSender mailsender) {
		EmailUtils.mailsender = mailsender;
	}
}
