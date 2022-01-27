package org.springblade.modules.process_low.wrapper;

import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.bean.vo.ProcessLowVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 11:40
 * @Description:
 */
public class ProcessLowWrapper  extends BaseEntityWrapper<ProcessLow, ProcessLowVO> {

	public static ProcessLowWrapper build() {
		return new ProcessLowWrapper();
	}

	@Override
	public ProcessLowVO entityVO(ProcessLow entity) {

		ProcessLowVO vo = entity == null ? null : CommonUtil.copy(entity, ProcessLowVO.class);
		return vo;
	}
}
