package org.springblade.modules.process_low.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.mapper.ProcessLowMapper;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springframework.stereotype.Service;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 11:23
 * @Description:
 */
@Service
public class ProcessLowServiceImpl extends ServiceImpl<ProcessLowMapper, ProcessLow> implements ProcessLowService {
	@Override
	public Boolean saveAndActiveTask(ProcessLow processLow) {
		boolean status = save(processLow);

		if (new Integer(0).equals(processLow.getType())) {
			// 开启 外购件不良 审批
		}else {
			// 开启 工序内不良 审批
			ApproveUtils.createTask(processLow.getId() + "", ApproveUtils.ApproveLinkEnum.PROCESS_LOW);
		}


		return status;
	}
}
