package org.springblade.modules.process_low.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.out_buy_low.mapper.OutBuyQprMapper;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.dto.RejectDTO;
import org.springblade.modules.process.enums.ApproveNodeStatusEnum;
import org.springblade.modules.process.service.BpmProcessLogService;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.mapper.ProcessLowMapper;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 11:23
 * @Description:
 */
@Service
public class ProcessLowServiceImpl extends ServiceImpl<ProcessLowMapper, ProcessLow> implements ProcessLowService {

	@Autowired
	private OutBuyQprMapper outBuyQprMapper;

	@Autowired
	private BpmProcessService processService;

	@Autowired
	private BpmProcessLogService logService;

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

	@Override
	public List<ProcessLow> getByIds(List<Long> qprIds) {
		LambdaQueryWrapper<ProcessLow> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(ProcessLow::getId, qprIds);
		return list(wrapper);
	}

	@Override
	@Transactional
	public void handlerQprReject(RejectDTO rejectDTO) {
		// 记录驳回状态
		// 记录日志
		BpmProcess process = processService.getByBusId(rejectDTO.getBusId());
		process.setBpmStatus(ApproveNodeStatusEnum.BACK.getCode());
		process.setBackCause(rejectDTO.getBackCause());
		setCommonOperator(process);
		logService.convertAndSave(process);

		// 删除当前业务流程
		LambdaUpdateWrapper<BpmProcess> deleteWrapper = new LambdaUpdateWrapper<>();
		deleteWrapper.eq(BpmProcess::getBusId, rejectDTO.getBusId());
		processService.remove(deleteWrapper);
		// 重新开启流程
		ApproveUtils.createTask(rejectDTO.getBusId() + "", ApproveUtils.ApproveLinkEnum.PROCESS_LOW);
	}

	private void setCommonOperator(BpmProcess bpmProcess) {
		bpmProcess.setOperatorTime(new Date());
		bpmProcess.setOperatorUser(CommonUtil.getUserId());
		bpmProcess.setOperatorUserName(CommonUtil.getUserName());
		bpmProcess.setOperatorDept(CommonUtil.getDeptId());
		bpmProcess.setOperatorDeptPath(CommonUtil.getDeptPath());
		bpmProcess.setOperatorRoleName(CommonUtil.getRoleName());
	}
}
