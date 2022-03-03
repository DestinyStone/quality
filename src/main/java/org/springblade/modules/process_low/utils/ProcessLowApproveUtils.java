package org.springblade.modules.process_low.utils;

import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.process.core.ProcessContainer;
import org.springblade.modules.process.enums.ApproveNodeStatusEnum;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/30 12:54
 * @Description:
 */
public class ProcessLowApproveUtils {

	public static List<ProcessContainer> getOutBuyProcessTaskList(String busId) {
		// 第一个节点 用户提交
		ProcessContainer userCommit = new ProcessContainer();
		userCommit.setBusId(busId);
		userCommit.setAccessDept(null);
		userCommit.setRemark("用户已提交");
		userCommit.setSort(0);
		userCommit.setFlag("commit");
		userCommit.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		userCommit.setStatus(ApproveNodeStatusEnum.SUCCESS.getCode());

		// 第二个节点 审批 qpr录入
		ProcessContainer qprSaevApprove = new ProcessContainer();
		qprSaevApprove.setBusId(busId);
		qprSaevApprove.setAccessDept(CommonUtil.getDeptId());
		qprSaevApprove.setRemark("不良联络书发行确认");
		qprSaevApprove.setSort(1);
		qprSaevApprove.setFlag("qprSave");
		qprSaevApprove.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		qprSaevApprove.setStatus(ApproveNodeStatusEnum.ACTIVE.getCode());

		// 第二个节点 不良联络书发行审批
		ProcessContainer qprApprove = new ProcessContainer();
		qprApprove.setBusId(busId);
		qprApprove.setAccessDept(CommonUtil.getDeptId());
		qprApprove.setRemark("不良联络书发行审批");
		qprApprove.setSort(2);
		qprApprove.setFlag("qprApprove");
		qprApprove.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		qprApprove.setStatus(ApproveNodeStatusEnum.UN_ACTIVE.getCode());

		// 第三个节点 审批 调查录入
		ProcessContainer checkApprove = new ProcessContainer();
		checkApprove.setBusId(busId);
		checkApprove.setAccessDept(CommonUtil.getDeptId());
		checkApprove.setRemark("调查结果录入");
		checkApprove.setSort(3);
		checkApprove.setFlag("checkSave");
		checkApprove.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		checkApprove.setStatus(ApproveNodeStatusEnum.UN_ACTIVE.getCode());

		// 第四个节点 调查结果确认
		ProcessContainer resultApprove = new ProcessContainer();
		resultApprove.setBusId(busId);
		resultApprove.setAccessDept(CommonUtil.getDeptId());
		resultApprove.setRemark("调查结果确认");
		resultApprove.setSort(4);
		resultApprove.setFlag("checkConfirm");
		resultApprove.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		resultApprove.setStatus(ApproveNodeStatusEnum.UN_ACTIVE.getCode());

		// 第五个节点 调查结果审批
		ProcessContainer approve = new ProcessContainer();
		approve.setBusId(busId);
		approve.setAccessDept(CommonUtil.getDeptId());
		approve.setRemark("调查结果审批");
		approve.setSort(5);
		approve.setFlag("checkApprove");
		approve.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		approve.setStatus(ApproveNodeStatusEnum.UN_ACTIVE.getCode());

		return Arrays.asList(userCommit, qprSaevApprove, qprApprove, checkApprove, resultApprove, approve);
	}

	public static List<ProcessContainer> getProcessTaskList(String busId) {
		// 第一个节点 用户提交
		ProcessContainer userCommit = new ProcessContainer();
		userCommit.setBusId(busId);
		userCommit.setAccessDept(null);
		userCommit.setRemark("用户已提交");
		userCommit.setSort(0);
		userCommit.setFlag("commit");
		userCommit.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		userCommit.setStatus(ApproveNodeStatusEnum.SUCCESS.getCode());

		// 第二个节点 审批
		ProcessContainer approve = new ProcessContainer();
		approve.setBusId(busId);
		approve.setAccessDept(CommonUtil.getDeptId());
		approve.setRemark("工序内不良审批");
		approve.setSort(1);
		approve.setFlag("processLowApprove");
		approve.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		approve.setStatus(ApproveNodeStatusEnum.ACTIVE.getCode());

		return Arrays.asList(userCommit, approve);
	}
}
