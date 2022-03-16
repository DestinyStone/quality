package org.springblade.modules.email.wrapper;

import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.email.bean.entity.EmailTemplate;
import org.springblade.modules.email.bean.vo.EmailTemplateVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:49
 * @Description:
 */
public class EmailTemplateWrapper extends BaseEntityWrapper<EmailTemplate, EmailTemplateVO> {

	public static EmailTemplateWrapper build() {
		return new EmailTemplateWrapper();
	}

	@Override
	public EmailTemplateVO entityVO(EmailTemplate entity) {
		EmailTemplateVO vo = entity == null ? null : CommonUtil.copy(entity, EmailTemplateVO.class);
		return vo;
	}
}
