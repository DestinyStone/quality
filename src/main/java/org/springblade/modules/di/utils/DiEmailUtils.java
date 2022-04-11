package org.springblade.modules.di.utils;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springblade.common.cache.UserCache;
import org.springblade.common.constant.EmailConstant;
import org.springblade.common.enums.EmailType;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.di.bean.entity.DiReport;
import org.springblade.modules.email.util.EmailTemplateUtils;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;

import javax.mail.MessagingException;
import java.util.HashMap;

/**
 * @Author: DestinyStone
 * @Date: 2022/4/4 16:07
 * @Description:
 */
public class DiEmailUtils {

	public static IUserService userService;

	static {
		userService = SpringUtil.getBean(IUserService.class);
	}

	public static void sendEmail(DiReport diReport) {
		Long createUser = diReport.getReportUser();
		User user = UserCache.getUser(createUser);
		if (StrUtil.isNotBlank(user.getEmail())) {
			try {
				HashMap<String, String> map = new HashMap<>();
				map.put("userName", user.getName());
				EmailTemplateUtils.send(EmailConstant.SUBMIT_DI_DATA, user.getEmail(), EmailType.QQ, map);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
}
