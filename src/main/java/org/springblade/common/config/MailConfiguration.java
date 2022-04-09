package org.springblade.common.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springblade.common.component.impl.QQMailSender;
import org.springblade.common.constant.ALiConstant;
import org.springblade.common.properties.MailProperties;
import org.springblade.common.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/16 17:39
 * @Description:
 */
@Configuration
public class MailConfiguration {

	@Autowired
	private MailProperties mailProperties;

	@Bean
	public static Client client() throws Exception {
		Config config = new Config()
			// 您的AccessKey ID
			.setAccessKeyId(ALiConstant.ACCESS_KEY_ID)
			// 您的AccessKey Secret
			.setAccessKeySecret(ALiConstant.ACCESS_KEY_SECRET);
		// 访问的域名
		config.endpoint = ALiConstant.ENDPOINT;
		return new Client(config);
	}

	@Bean
	public QQMailSender qqMailSender() {
		// 邮件发送者
		QQMailSender javaMailSender = new QQMailSender();
		javaMailSender.setDefaultEncoding(mailProperties.getDefaultEncoding().toString());
		javaMailSender.setHost(mailProperties.getHost());
		javaMailSender.setPort(mailProperties.getPort());
		javaMailSender.setProtocol(mailProperties.getProtocol());
		javaMailSender.setUsername(mailProperties.getUsername());
		javaMailSender.setPassword(mailProperties.getPassword());
		javaMailSender.setJavaMailProperties(CommonUtil.mapToProperties(mailProperties.getProperties()));

		return javaMailSender;
	}

}
