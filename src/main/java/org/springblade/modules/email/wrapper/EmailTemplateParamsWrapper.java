package org.springblade.modules.email.wrapper;

import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.email.bean.entity.EmailTemplateParam;
import org.springblade.modules.email.bean.vo.EmailTemplateParamVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:49
 * @Description:
 */
public class EmailTemplateParamsWrapper extends BaseEntityWrapper<EmailTemplateParam, EmailTemplateParamVO> {

	public static EmailTemplateParamsWrapper build() {
		return new EmailTemplateParamsWrapper();
	}

	@Override
	public EmailTemplateParamVO entityVO(EmailTemplateParam entity) {
		EmailTemplateParamVO vo = entity == null ? null : CommonUtil.copy(entity, EmailTemplateParamVO.class);
		return vo;
	}
}
