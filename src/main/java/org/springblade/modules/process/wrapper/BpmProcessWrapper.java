package org.springblade.modules.process.wrapper;

import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.vo.BpmProcessVO;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/1 13:52
 * @Description:
 */
public class BpmProcessWrapper extends BaseEntityWrapper<BpmProcess, BpmProcessVO> {

	public static BpmProcessWrapper build() {
		return new BpmProcessWrapper();
	}

	@Override
	public BpmProcessVO entityVO(BpmProcess entity) {

		BpmProcessVO vo = entity == null ? null : CommonUtil.copy(entity, BpmProcessVO.class);
		return vo;
	}
}
