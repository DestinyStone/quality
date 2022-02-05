package org.springblade.modules.process.wrapper;

import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.process.entity.bean.BpmProcessLog;
import org.springblade.modules.process.entity.vo.BpmProcessLogVO;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 14:58
 * @Description:
 */
public class BpmProcessLogWrapper  extends BaseEntityWrapper<BpmProcessLog, BpmProcessLogVO> {

	public static BpmProcessLogWrapper build() {
		return new BpmProcessLogWrapper();
	}

	@Override
	public BpmProcessLogVO entityVO(BpmProcessLog entity) {

		BpmProcessLogVO vo = entity == null ? null : CommonUtil.copy(entity, BpmProcessLogVO.class);
		return vo;
	}
}
