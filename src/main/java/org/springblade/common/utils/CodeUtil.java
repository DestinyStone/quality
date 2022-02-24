package org.springblade.common.utils;

import org.springblade.modules.codeing.service.BusCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 11:19
 * @Description: 唯一码获取
 */
@Component
public class CodeUtil {

	private static BusCodeService codeService;

	public static String getCode(String flag) {
		return codeService.getCode(flag);
	}

	@Autowired
	public void setCodeService(BusCodeService codeService) {
		CodeUtil.codeService = codeService;
	}

}
