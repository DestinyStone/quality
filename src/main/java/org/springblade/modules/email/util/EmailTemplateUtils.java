package org.springblade.modules.email.util;

import cn.hutool.core.util.StrUtil;
import org.springblade.common.enums.EmailType;
import org.springblade.common.utils.CommonUtil;
import org.springblade.common.utils.EmailUtils;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.email.bean.entity.EmailTemplate;
import org.springblade.modules.email.service.IEmailTemplateService;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DestinyStone
 * @Date: 2022/4/4 16:12
 * @Description:
 */
public class EmailTemplateUtils {

	public static IEmailTemplateService emailTemplateService;

	public static final String EXCEPTION_WARNING_TEMPLATE = "邮件模板:{} 未启用";

	static {
		emailTemplateService = SpringUtil.getBean(IEmailTemplateService.class);
	}

	public static void send(String code, String to, EmailType type) throws MessagingException {
		send(code, to, type, new HashMap<>());
	}

	public static void send(String code, String to, EmailType type, HashMap<String, String> map)throws MessagingException  {
		EmailTemplate detail = emailTemplateService.getByCode(code);
		if (detail == null) {
			throw new ServiceException("邮件模板不存在");
		}

		if (!new Integer(1).equals(detail.getStatus())) {
			throw new ServiceException(StrUtil.format(EXCEPTION_WARNING_TEMPLATE, detail.getTitle(), detail.getCode()));
		}

		EmailUtils.send(detail.getTitle(), handlerContent(detail.getContent(), map), to, type);
	}

	private static String handlerContent(String template, Map<String ,String> map) {
		return CommonUtil.placeHolderReplace(template, map);
	}
}
