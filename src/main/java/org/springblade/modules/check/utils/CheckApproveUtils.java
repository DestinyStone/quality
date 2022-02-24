package org.springblade.modules.check.utils;

import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.process.core.ProcessContainer;
import org.springblade.modules.process.enums.ApproveNodeStatusEnum;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/23 18:16
 * @Description:
 */
public class CheckApproveUtils {
	public static List<ProcessContainer> getProcessTaskList(String busId) {
		// 第一个节点 用户提交
		ProcessContainer userCommit = new ProcessContainer();
		userCommit.setBusId(busId);
		userCommit.setAccessDept(null);
		userCommit.setRemark("用户已提交");
		userCommit.setSort(0);
		userCommit.setFlag("commit");
		userCommit.setServerFlag(ApproveUtils.ServerFlagEnum.CHECK_APPROVE.getMessage());
		userCommit.setStatus(ApproveNodeStatusEnum.SUCCESS.getCode());

		// 第二个节点 担当 审批
		ProcessContainer downLoadResourceApprove = new ProcessContainer();
		downLoadResourceApprove.setBusId(busId);
		downLoadResourceApprove.setAccessDept(CommonUtil.getDeptId());
		downLoadResourceApprove.setRemark("担当审批");
		downLoadResourceApprove.setSort(1);
		downLoadResourceApprove.setFlag("downloadResourceFile");
		downLoadResourceApprove.setServerFlag(ApproveUtils.ServerFlagEnum.CHECK_APPROVE.getMessage());
		downLoadResourceApprove.setStatus(ApproveNodeStatusEnum.ACTIVE.getCode());

		// 第三个节点 领导审批
		ProcessContainer bossProcess = new ProcessContainer();
		bossProcess.setBusId(busId);
		bossProcess.setAccessDept(CommonUtil.getDeptId());
		bossProcess.setRemark("调查结果录入");
		bossProcess.setFlag("boosProcess");
		bossProcess.setSort(2);
		bossProcess.setServerFlag(ApproveUtils.ServerFlagEnum.CHECK_APPROVE.getMessage());
		bossProcess.setStatus(ApproveNodeStatusEnum.UN_ACTIVE.getCode());

		return Arrays.asList(userCommit, downLoadResourceApprove, bossProcess);
	}
}
