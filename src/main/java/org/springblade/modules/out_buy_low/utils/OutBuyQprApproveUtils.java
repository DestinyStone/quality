package org.springblade.modules.out_buy_low.utils;

import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.process.core.ProcessContainer;
import org.springblade.modules.process.enums.ApproveNodeStatusEnum;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/1 18:13
 * @Description:
 */
public class OutBuyQprApproveUtils {
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

		// 第二个节点 不良联络书发行审批
		ProcessContainer qprApprove = new ProcessContainer();
		qprApprove.setBusId(busId);
		qprApprove.setAccessDept(CommonUtil.getDeptId());
		qprApprove.setRemark("不良联络书发行审批");
		qprApprove.setSort(1);
		qprApprove.setFlag("qprApprove");
		qprApprove.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		qprApprove.setStatus(ApproveNodeStatusEnum.ACTIVE.getCode());

		// 第三个节点 审批 调查录入
		ProcessContainer checkApprove = new ProcessContainer();
		checkApprove.setBusId(busId);
		checkApprove.setAccessDept(CommonUtil.getDeptId());
		checkApprove.setRemark("调查结果录入");
		checkApprove.setFlag("checkSave");
		checkApprove.setSort(2);
		checkApprove.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		checkApprove.setStatus(ApproveNodeStatusEnum.UN_ACTIVE.getCode());

		// 第四个节点 调查结果确认
		ProcessContainer resultApprove = new ProcessContainer();
		resultApprove.setBusId(busId);
		resultApprove.setAccessDept(CommonUtil.getDeptId());
		resultApprove.setRemark("调查结果确认");
		resultApprove.setSort(3);
		resultApprove.setFlag("checkConfirm");
		resultApprove.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		resultApprove.setStatus(ApproveNodeStatusEnum.UN_ACTIVE.getCode());

		// 第五个节点 调查结果审批
		ProcessContainer approve = new ProcessContainer();
		approve.setBusId(busId);
		approve.setAccessDept(CommonUtil.getDeptId());
		approve.setRemark("调查结果审批");
		approve.setSort(4);
		approve.setServerFlag(ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage());
		approve.setFlag("checkApprove");
		approve.setStatus(ApproveNodeStatusEnum.UN_ACTIVE.getCode());

		return Arrays.asList(userCommit, qprApprove, checkApprove, resultApprove, approve);
	}
}
