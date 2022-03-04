package org.springblade.modules.di.wrapper;

import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.di.bean.entity.DiConfig;
import org.springblade.modules.di.bean.vo.DiConfigVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 10:09
 * @Description:
 */
public class DiConfigWrapper extends BaseEntityWrapper<DiConfig, DiConfigVO> {

	public static DiConfigWrapper build() {
		return new DiConfigWrapper();
	}

	@Override
	public DiConfigVO entityVO(DiConfig entity) {
		DiConfigVO vo = entity == null ? null : CommonUtil.copy(entity, DiConfigVO.class);
		return vo;
	}
}
