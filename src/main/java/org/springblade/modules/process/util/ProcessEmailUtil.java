package org.springblade.modules.process.util;

import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.system.service.IUserService;

/**
 * @Author: DestinyStone
 * @Date: 2022/4/4 12:26
 * @Description:
 */
public class ProcessEmailUtil {

	private static IUserService userService;

	static {
		userService = SpringUtil.getBean(IUserService.class);
	}



	public static void send(BpmProcess process) {
		String dept = process.getAccessDept();
		String role = process.getAccessDept();


	}
}
