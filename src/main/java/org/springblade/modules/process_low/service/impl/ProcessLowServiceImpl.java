package org.springblade.modules.process_low.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.modules.out_buy_low.mapper.OutBuyQprMapper;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.mapper.ProcessLowMapper;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 11:23
 * @Description:
 */
@Service
public class ProcessLowServiceImpl extends ServiceImpl<ProcessLowMapper, ProcessLow> implements ProcessLowService {

	@Autowired
	private OutBuyQprMapper outBuyQprMapper;

	@Override
	public Boolean saveAndActiveTask(ProcessLow processLow) {
		boolean status = save(processLow);

		if (new Integer(0).equals(processLow.getType())) {
			// 开启 外购件不良 审批
			// 录入用于审批的qpr信息
//			OutBuyQpr outBuyQpr = BeanUtil.copyProperties(processLow, OutBuyQpr.class);
//			outBuyQpr.setId(null);
//			outBuyQpr.setProcessLowFlag(1);
//			outBuyQpr.setProcessLowId(processLow.getId());
//			outBuyQprMapper.insert(outBuyQpr);

			// 开启审批流程
			ApproveUtils.createTask(processLow.getId() + "", ApproveUtils.ApproveLinkEnum.PROCESS_LOW_OUT_BUY);
		}else {
			// 开启 工序内不良 审批
			ApproveUtils.createTask(processLow.getId() + "", ApproveUtils.ApproveLinkEnum.PROCESS_LOW);
		}


		return status;
	}
}
