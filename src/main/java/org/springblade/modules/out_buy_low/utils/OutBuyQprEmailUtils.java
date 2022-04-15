package org.springblade.modules.out_buy_low.utils;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springblade.common.cache.UserCache;
import org.springblade.common.constant.EmailConstant;
import org.springblade.common.enums.EmailType;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.email.util.EmailTemplateUtils;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;

import javax.mail.MessagingException;
import java.util.HashMap;

/**
 * @Author: DestinyStone
 * @Date: 2022/4/5 16:25
 * @Description:
 */
public class OutBuyQprEmailUtils {
	public static IUserService  userService;

	static {
		userService = SpringUtil.getBean(IUserService.class);
	}

	public static void sendCompleteWarning(OutBuyQpr outBuyQpr) {
		sendWarning(outBuyQpr, EmailConstant.OUT_BUY_LOW_COMPLETE);
	}

	public static void sendWarningEmail(OutBuyQpr outBuyQpr) {
		sendWarning(outBuyQpr, EmailConstant.SUBMIT_OUT_BUY_LOW);
	}

	private static void sendWarning(OutBuyQpr outBuyQpr, String constant) {
		Long createUser = outBuyQpr.getCreateUser();
		User user = UserCache.getUser(createUser);
		if (StrUtil.isNotBlank(user.getEmail())) {
			try {
				HashMap<String, String> map = new HashMap<>();
				map.put("userName", user.getName());
				map.put("title", outBuyQpr.getTitle());
				EmailTemplateUtils.send(constant, user.getEmail(), EmailType.QQ, map);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
}
