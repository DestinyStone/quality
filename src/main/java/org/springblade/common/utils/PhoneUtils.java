package org.springblade.common.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.Map;

/**
 * @Author: xiaoxia
 * @Date: 2022/4/8 11:05
 * @Description:
 */
public class PhoneUtils {

	private static Client client;
	private static final String SINGLE = "嘤嘤怪的网站";
	static {
		client = SpringUtil.getBean(Client.class);
	}

	public static void sendEmail(String code, String to, Map<String, String> map) throws Exception {
		SendSmsRequest sendSmsRequest = new SendSmsRequest()
			.setSignName(SINGLE)
			.setTemplateCode(code)
			.setPhoneNumbers(to)
			.setTemplateParam(CommonUtil.mapToJson(map).toJSONString());
		client.sendSms(sendSmsRequest);
	}

	public static void sendEmailSync(String code, String to, Map<String, String> map) {
		SendSmsRequest sendSmsRequest = new SendSmsRequest()
			.setSignName(SINGLE)
			.setTemplateCode(code)
			.setPhoneNumbers(to)
			.setTemplateParam(CommonUtil.mapToJson(map).toJSONString());
		new Thread(() -> {
			try {
				client.sendSms(sendSmsRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
}
