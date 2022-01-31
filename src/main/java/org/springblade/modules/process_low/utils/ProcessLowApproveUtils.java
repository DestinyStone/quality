package org.springblade.modules.process_low.utils;

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

	public static List<ProcessContainer> getProcessTaskList(String busId) {
		// 第一个节点 用户提交
		ProcessContainer userCommit = new ProcessContainer();
		userCommit.setBusId(busId);
		userCommit.setAccessDept(null);
		userCommit.setRemark("用户已提交");
		userCommit.setSort(0);
		userCommit.setStatus(ApproveNodeStatusEnum.SUCCESS.getCode());

		// 第二个节点 审批
		ProcessContainer approve = new ProcessContainer();
		approve.setBusId(busId);
		approve.setAccessDept(CommonUtil.getDeptId());
		approve.setRemark("工序内不良审批");
		approve.setSort(1);
		approve.setStatus(ApproveNodeStatusEnum.ACTIVE.getCode());

		return Arrays.asList(userCommit, approve);
	}
}
