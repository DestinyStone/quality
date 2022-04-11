package org.springblade.modules.check.utils;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springblade.common.cache.UserCache;
import org.springblade.common.constant.EmailConstant;
import org.springblade.common.enums.EmailType;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.email.util.EmailTemplateUtils;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/4/4 16:07
 * @Description:
 */
public class CheckEmailUtils {

	public static IUserService userService;

	static {
		userService = SpringUtil.getBean(IUserService.class);
	}

	public static void sendEmail(Check check) {
		Long createUser = check.getCreateUser();
		User user = UserCache.getUser(createUser);
		if (StrUtil.isNotBlank(user.getEmail())) {
			try {
				HashMap<String, String> map = new HashMap<>();
				map.put("userName", user.getName());
				EmailTemplateUtils.send(EmailConstant.SUBMIT_OUT_BUY_LOW, user.getEmail(), EmailType.QQ, map);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendWarningEmail(Check check) {
		String dutyDept = check.getDutyDept();
		if (StrUtil.isBlank(dutyDept)) {
			throw new ServiceException("供应商不存在");
		}
		List<User> user = userService.getByProviderId(dutyDept);
		for (User item : user) {
			if (StrUtil.isNotBlank(item.getEmail())) {
				try {
					EmailTemplateUtils.send(EmailConstant.INSPECTION_MAKE_REMIND, item.getEmail(), EmailType.QQ);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
