package org.springblade.modules.work.wrapper;

import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.work.entity.bean.SettleLog;
import org.springblade.modules.work.entity.vo.SettleLogVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:49
 * @Description:
 */
public class SettleLogWrapper extends BaseEntityWrapper<SettleLog, SettleLogVO> {

	public static SettleLogWrapper build() {
		return new SettleLogWrapper();
	}

	@Override
	public SettleLogVO entityVO(SettleLog entity) {
		SettleLogVO vo = entity == null ? null : CommonUtil.copy(entity, SettleLogVO.class);
		return vo;
	}
}
