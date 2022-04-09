package org.springblade.modules.phone.wrapper;

import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.phone.bean.entity.PhoneTemplateParam;
import org.springblade.modules.phone.bean.vo.PhoneTemplateParamVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:49
 * @Description:
 */
public class PhoneTemplateParamsWrapper extends BaseEntityWrapper<PhoneTemplateParam, PhoneTemplateParamVO> {

	public static PhoneTemplateParamsWrapper build() {
		return new PhoneTemplateParamsWrapper();
	}

	@Override
	public PhoneTemplateParamVO entityVO(PhoneTemplateParam entity) {
		PhoneTemplateParamVO vo = entity == null ? null : CommonUtil.copy(entity, PhoneTemplateParamVO.class);
		return vo;
	}
}
