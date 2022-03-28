package org.springblade.modules.di.utils;

import org.springblade.common.cache.ParamCache;
import org.springblade.common.constant.ParamConstant;
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
public class DiApproveUtils {

	public static List<ProcessContainer> getUnReportProcessTaskList(String busId) {
		// 第一个节点 用户提交
		ProcessContainer userCommit =new ProcessContainer(
			busId,
			"-1",
			"-1",
			"用户已提交",
			0,
			"commit",
			ApproveUtils.ServerFlagEnum.DI_APPROVE.getMessage(),
			ApproveNodeStatusEnum.SUCCESS.getCode());

		// 第二个节点 系长，科长 审批
		ProcessContainer downLoadResourceApprove = new ProcessContainer(
			busId,
			CommonUtil.getDeptId() + "",
			Arrays.asList(ParamCache.getValue(ParamConstant.CHIEF), ParamCache.getValue(ParamConstant.DEPT)),
			"系长,科长审批",
			1,
			"unReportProcess",
			ApproveUtils.ServerFlagEnum.DI_APPROVE.getMessage(),
			ApproveNodeStatusEnum.ACTIVE.getCode());
		return Arrays.asList(userCommit, downLoadResourceApprove);
	}

	public static List<ProcessContainer> getProcessTaskList(String busId) {
		// 第一个节点 用户提交
		ProcessContainer userCommit =new ProcessContainer(
			busId,
			"-1",
			"-1",
			"用户已提交",
			0,
			"commit",
			ApproveUtils.ServerFlagEnum.DI_APPROVE.getMessage(),
			ApproveNodeStatusEnum.SUCCESS.getCode());

		// 第二个节点 系长，科长 审批
		ProcessContainer downLoadResourceApprove = new ProcessContainer(
			busId,
			CommonUtil.getDeptId() + "",
			ParamCache.getValue(ParamConstant.CHIEF),
			"系长审批",
			1,
			"unReportProcess",
			ApproveUtils.ServerFlagEnum.DI_APPROVE.getMessage(),
			ApproveNodeStatusEnum.ACTIVE.getCode());

		// 第三个节点 科长审批
		ProcessContainer bossProcess = new ProcessContainer(
			busId,
			CommonUtil.getDeptId() + "",
			ParamCache.getValue(ParamConstant.DEPT),
			"系长审批",
			2,
			"boosProcess",
			ApproveUtils.ServerFlagEnum.DI_APPROVE.getMessage(),
			ApproveNodeStatusEnum.UN_ACTIVE.getCode());

		return Arrays.asList(userCommit, downLoadResourceApprove, bossProcess);
	}
}
