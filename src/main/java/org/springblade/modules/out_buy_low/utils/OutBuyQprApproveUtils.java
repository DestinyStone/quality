package org.springblade.modules.out_buy_low.utils;

import org.springblade.common.cache.ParamCache;
import org.springblade.common.constant.ParamConstant;
import org.springblade.common.utils.ApproveUtils;
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
		ProcessContainer userCommit = new ProcessContainer(
			busId,
			"-1",
			"-1",
			"用户已提交",
			0,
			"commit",
			ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage(),
			ApproveNodeStatusEnum.SUCCESS.getCode()
		);

		// 第二个节点 不良联络书发行审批
		ProcessContainer qprApprove = new ProcessContainer(
			busId,
			ParamCache.getValue(ParamConstant.QC_SECTION_OUT_BUY),
			ParamCache.getValue(ParamConstant.QC_SECTION_OUT_BUY_DEPT),
			"不良联络书发行审批",
			1,
			"qprApprove",
			ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage(),
			ApproveNodeStatusEnum.ACTIVE.getCode()
		);

		// 第三个节点 审批 调查录入
		ProcessContainer checkApprove = new ProcessContainer(
			busId,
			"0",
			ParamConstant.PROVIDER_REPLACE,
			"不良调查",
			2,
			"checkSave",
			ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage(),
			ApproveNodeStatusEnum.UN_ACTIVE.getCode()
		);


		// 第四个节点 调查结果确认
		ProcessContainer resultApprove = new ProcessContainer(
			busId,
			ParamCache.getValue(ParamConstant.QC_SECTION_OUT_BUY),
			ParamCache.getValue(ParamConstant.QC_SECTION_OUT_BUY_TAKE),
			"调查结果确认",
			3,
			"checkConfirm",
			ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage(),
			ApproveNodeStatusEnum.UN_ACTIVE.getCode()
		);

		// 第五个节点 调查结果审批
		ProcessContainer approve = new ProcessContainer(
			busId,
			ParamCache.getValue(ParamConstant.QC_SECTION_OUT_BUY),
			ParamCache.getValue(ParamConstant.QC_SECTION_OUT_BUY_DEPT),
			"调查结果审批",
			4,
			"checkApprove",
			ApproveUtils.ServerFlagEnum.LOW_APPROVE.getMessage(),
			ApproveNodeStatusEnum.UN_ACTIVE.getCode()
		);

		return Arrays.asList(userCommit, qprApprove, checkApprove, resultApprove, approve);
	}
}
