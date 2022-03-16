package org.springblade.common.component.impl;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springblade.common.cache.ParamCache;
import org.springblade.common.component.AbstractMailSender;
import org.springblade.common.constant.ParamConstant;
import org.springblade.common.enums.EmailType;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/16 17:51
 * @Description:
 */
public class QQMailSender extends AbstractMailSender {

	@Override
	public void resetUsername() {
		String password = ParamCache.getValue(ParamConstant.QQ_USERNAME);
		if (StrUtil.isNotBlank(password)) {
			setPassword(password);
		}
	}

	@Override
	public void resetPassword() {
		String password = ParamCache.getValue(ParamConstant.QQ_PASSWORD);
		if (StrUtil.isNotBlank(password)) {
			setPassword(password);
		}
	}

	@Override
	public EmailType getType() {
		return EmailType.QQ;
	}
}
