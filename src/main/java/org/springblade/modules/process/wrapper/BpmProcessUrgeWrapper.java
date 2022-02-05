package org.springblade.modules.process.wrapper;

import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.process.entity.bean.BpmProcessUrge;
import org.springblade.modules.process.entity.vo.BpmProcessUrgeVO;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 11:57
 * @Description:
 */

public class BpmProcessUrgeWrapper  extends BaseEntityWrapper<BpmProcessUrge, BpmProcessUrgeVO> {

	public static BpmProcessUrgeWrapper build() {
		return new BpmProcessUrgeWrapper();
	}

	@Override
	public BpmProcessUrgeVO entityVO(BpmProcessUrge entity) {

		BpmProcessUrgeVO vo = entity == null ? null : CommonUtil.copy(entity, BpmProcessUrgeVO.class);
		return vo;
	}
}
