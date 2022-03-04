package org.springblade.modules.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.modules.process.core.ProcessContainer;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.enums.ApproveNodeStatusEnum;
import org.springblade.modules.process.mapper.BpmProcessMapper;
import org.springblade.modules.process.service.BpmProcessLogService;
import org.springblade.modules.process.service.BpmProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/29 00:35
 * @Description:
 */
@Service
public class BpmProcessServiceImpl extends ServiceImpl<BpmProcessMapper, BpmProcess> implements BpmProcessService {

	/**
	 * 超时时间 7天
	 */
	private static final Integer OVER_TIME = 1000 * 60 * 60 * 24 * 7;

	@Autowired
	private BpmProcessLogService logService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createTask(List<ProcessContainer> list) {
		ArrayList<BpmProcess> processes = new ArrayList<>();
		Date createTime = new Date();
		for (ProcessContainer item : list) {
			BpmProcess bpmProcess = new BpmProcess();
			bpmProcess.setBpmRemark(item.getRemark());
			bpmProcess.setBpmStatus(item.getStatus());
			bpmProcess.setBpmSort(item.getSort());
			bpmProcess.setBusId(item.getBusId());
			bpmProcess.setBpmFlag(item.getFlag());
			bpmProcess.setBpmServerFlag(item.getServerFlag());
			// TODO 允许的部门， 后续需要增进接口
			bpmProcess.setAccessDept(item.getAccessDept());
			bpmProcess.setBpmBingId(CommonUtil.getCode() + "");
			bpmProcess.setIsCastoff(0);
			// 对已完成的任务设置基本属性
			if (ApproveNodeStatusEnum.SUCCESS.getCode().equals(item.getStatus())) {
				bpmProcess.setCreateTime(createTime);
				bpmProcess.setEndTime(new Date(createTime.getTime() + OVER_TIME));
				setCommonOperator(bpmProcess);
				bpmProcess.setBpmPushStatus(0);
				logService.convertAndSave(bpmProcess);
			}

			// 对已激活的任务设置超时时间
			if (ApproveNodeStatusEnum.ACTIVE.getCode().equals(item.getStatus())) {
				bpmProcess.setCreateTime(createTime);
				bpmProcess.setEndTime(new Date(createTime.getTime() + OVER_TIME));
				bpmProcess.setBpmPushStatus(0);
			}

			processes.add(bpmProcess);


		}
		this.saveBatch(processes);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void pass(Long bpmId) {
		BpmProcess process = getByIdOrException(bpmId);
		if (process.getEndTime() != null && System.currentTimeMillis() > process.getEndTime().getTime()) {
			throw new ServiceException("审批已截止");
		}

		if (process.getBpmStatus().equals(ApproveNodeStatusEnum.SUCCESS.getCode())) {
			throw new ServiceException("当前节点已审批通过");
		}

		// 更新当前节点为激活状态
		BpmProcess currentUpdate = new BpmProcess();
		currentUpdate.setBpmId(process.getBpmId());
		currentUpdate.setBpmStatus(ApproveNodeStatusEnum.SUCCESS.getCode());
		setCommonOperator(currentUpdate);
		updateById(currentUpdate);

		// 开启下一个节点审批
		Date createTime = new Date();
		LambdaUpdateWrapper<BpmProcess> nextProcessUpdate = new LambdaUpdateWrapper<>();
		nextProcessUpdate.eq(BpmProcess::getBusId, process.getBusId())
			.ne(BpmProcess::getBpmStatus, 0)
			.eq(BpmProcess::getBpmSort, process.getBpmSort() + 1)
			.eq(BpmProcess::getIsCastoff, 0)
			.set(BpmProcess::getBpmStatus, ApproveNodeStatusEnum.ACTIVE.getCode())
			.set(BpmProcess::getCreateTime, createTime)
			.set(BpmProcess::getEndTime, new Date(createTime.getTime() + OVER_TIME))
			.set(BpmProcess::getBpmPushStatus, 0);
		update(nextProcessUpdate);

		// 记录日志
		process.setBpmStatus(ApproveNodeStatusEnum.SUCCESS.getCode());
		setCommonOperator(process);
		logService.convertAndSave(process);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void reject(Long bpmId, String cause) {
		BpmProcess process = getByIdOrException(bpmId);

		// 更新当前节点状态
		BpmProcess currentUpdate = new BpmProcess();
		currentUpdate.setBpmId(process.getBpmId());
		currentUpdate.setBpmStatus(ApproveNodeStatusEnum.BACK.getCode());
		currentUpdate.setBackCause(cause);
		setCommonOperator(currentUpdate);

		updateById(currentUpdate);

		// 记录日志
		process.setBpmStatus(ApproveNodeStatusEnum.BACK.getCode());
		process.setBackCause(cause);
		setCommonOperator(process);
		logService.convertAndSave(process);
	}

	@Override
	public Boolean isProcessEnd(Long bpmId) {
		BpmProcess process = getByIdOrException(bpmId);
		LambdaQueryWrapper<BpmProcess> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcess::getBusId, process.getBusId())
			.eq(BpmProcess::getIsCastoff, 0)
			.ne(BpmProcess::getBpmStatus, ApproveNodeStatusEnum.SUCCESS.getCode());

		return count(wrapper) == 0;
	}

	@Override
	public void delete(Long id) {
		LambdaQueryWrapper<BpmProcess> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcess::getBusId, id);
		remove(wrapper);
	}

	@Override
	public BpmProcess getByBusId(Long id) {
		LambdaQueryWrapper<BpmProcess> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcess::getBpmStatus, ApproveNodeStatusEnum.ACTIVE.getCode())
			.eq(BpmProcess::getBusId, id)
			.eq(BpmProcess::getIsCastoff, 0);
		BpmProcess process = this.getOne(wrapper);

		if(process == null) {
			throw new ServiceException("当前审批节点不存在");
		}
		return process;
	}

	private BpmProcess getByIdOrException(Long bpmId) {
		BpmProcess process = getById(bpmId);
		if (process == null) {
			throw new ServiceException("当前审批节点不存在");
		}
		return process;
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
