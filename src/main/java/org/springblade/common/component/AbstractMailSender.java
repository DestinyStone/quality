package org.springblade.common.component;

import org.springblade.common.enums.EmailType;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/16 18:01
 * @Description:
 */
public abstract class AbstractMailSender extends JavaMailSenderImpl {

	public void reset() {
		this.resetUsername();
		this.resetPassword();
	}

	public abstract void resetUsername();

	public abstract void resetPassword();

	public abstract EmailType getType();
}
