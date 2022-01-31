package org.springblade.common.utils;

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

	public static enum ApproveLinkEnum {
		/**
		 * 工序内不良审批
		 */
		PROCESS_LOW,

		/**
		 * 外购件不良审批
		 */
		OUT_BUY_LOW,
		;
	}

	public static void createTask(String busId, ApproveLinkEnum linkEnum) {
		if (ApproveLinkEnum.PROCESS_LOW.equals(linkEnum)) {
			List<ProcessContainer> processTaskList = ProcessLowApproveUtils.getProcessTaskList(busId + "");
			createTask(processTaskList);
		}

		if (ApproveLinkEnum.OUT_BUY_LOW.equals(linkEnum)) {
			// TODO
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
