package org.springblade.modules.process_low.utils;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springblade.common.cache.UserCache;
import org.springblade.common.constant.EmailConstant;
import org.springblade.common.enums.EmailType;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.email.util.EmailTemplateUtils;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;

import javax.mail.MessagingException;
import java.util.HashMap;

/**
 * @Author: DestinyStone
 * @Date: 2022/4/5 16:25
 * @Description:
 */
public class ProcessLowEmailUtils {
	public static IUserService userService;

	static {
		userService = SpringUtil.getBean(IUserService.class);
	}

	public static void sendCompleteWarningEmail(ProcessLow processLow) {
		sendWarning(processLow, EmailConstant.PROCESS_LOW_COMPLETE);
	}

	public static void sendWarningEmail(ProcessLow processLow) {
		sendWarning(processLow, EmailConstant.SUBMIT_PROCESS_LOW);
	}

	private static void sendWarning(ProcessLow processLow, String constant) {
		Long createUser = processLow.getCreateUser();
		User user = UserCache.getUser(createUser);
		if (StrUtil.isNotBlank(user.getEmail())) {
			try {
				HashMap<String, String> map = new HashMap<>();
				map.put("userName", user.getName());
				map.put("title", processLow.getTitle());
				EmailTemplateUtils.send(constant, user.getEmail(), EmailType.QQ, map);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
}
