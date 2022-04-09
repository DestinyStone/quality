package org.springblade.modules.phone.wrapper;

import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.phone.bean.entity.PhoneTemplate;
import org.springblade.modules.phone.bean.vo.PhoneTemplateVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:49
 * @Description:
 */
public class PhoneTemplateWrapper extends BaseEntityWrapper<PhoneTemplate, PhoneTemplateVO> {

	public static PhoneTemplateWrapper build() {
		return new PhoneTemplateWrapper();
	}

	@Override
	public PhoneTemplateVO entityVO(PhoneTemplate entity) {
		PhoneTemplateVO vo = entity == null ? null : CommonUtil.copy(entity, PhoneTemplateVO.class);
		return vo;
	}
}
