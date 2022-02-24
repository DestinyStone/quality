package org.springblade.common.utils;

import org.springblade.modules.check.utils.CheckApproveUtils;
import org.springblade.modules.out_buy_low.utils.OutBuyQprApproveUtils;
import org.springblade.modules.process.core.ProcessContainer;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process_low.utils.ProcessLowApproveUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/30 13:03
 * @Description: 审批工具类
 */
@Component
public class ApproveUtils {

	private static BpmProcessService processService;

	public static enum ServerFlagEnum {
		/**
		 * 不良审批
		 */
		LOW_APPROVE("lowApprove"),

		/**
		 * 检查法审批
		 */
		CHECK_APPROVE("checkApprove"),
		;
		String message;

		ServerFlagEnum(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	public static enum ApproveLinkEnum {
		/**
		 * 工序内不良审批
		 */
		PROCESS_LOW,

		/**
		 * 外购件不良审批
		 */
		OUT_BUY_LOW,

		/**
		 * 工序内不良外购件审批
		 */
		PROCESS_LOW_OUT_BUY,

		/**
		 * 检查法审批
		 */
		CHECK,
		;
	}

	public static void createTask(String busId, ApproveLinkEnum linkEnum) {
		List<ProcessContainer> processTaskList = null;
		if (ApproveLinkEnum.PROCESS_LOW.equals(linkEnum)) {
			processTaskList = ProcessLowApproveUtils.getProcessTaskList(busId + "");
		}

		if (ApproveLinkEnum.OUT_BUY_LOW.equals(linkEnum)) {
			processTaskList = OutBuyQprApproveUtils.getProcessTaskList(busId + "");
		}

		if (ApproveLinkEnum.PROCESS_LOW_OUT_BUY.equals(linkEnum)) {
			processTaskList = ProcessLowApproveUtils.getOutBuyProcessTaskList(busId + "");
		}

		if (ApproveLinkEnum.CHECK.equals(linkEnum)) {
			processTaskList = CheckApproveUtils.getProcessTaskList(busId);
		}

		if (processTaskList != null) {
			createTask(processTaskList);
		}
	}

	public static void createTask(List<ProcessContainer> list) {
			processService.createTask(list);
	}

	@Autowired
	public void setProcessService(BpmProcessService processService) {
		ApproveUtils.processService = processService;
	}
}
