package org.springblade.modules.account.util;

import org.springblade.modules.codeing.bean.entity.BusCode;
import org.springblade.modules.codeing.service.BusCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/24 19:20
 * @Description:
 */
@Component
public class AccountCodeUtil {
	private static BusCodeService codeService;
	private static final String FLAG = "ACCOUNT";
	private static final String DEFAULT_PREFIX = "96";
	private static final Long START_CODE = 1L;
	private static final Integer PREFIX_COUNT = 9;

	public synchronized static String getCode(String separate) {
		BusCode flagCode = codeService.getBySeparate(separate, FLAG);
		if (flagCode == null) {
			BusCode busCode = new BusCode();
			busCode.setPrefix(DEFAULT_PREFIX);
			busCode.setCode(START_CODE);
			busCode.setSeparate(separate);
			busCode.setUpdateTime(new Date());
			busCode.setFlag(FLAG);
			codeService.save(busCode);
			return convertCode(DEFAULT_PREFIX, START_CODE);
		}

		flagCode.setUpdateTime(new Date());
		if (flagCode.getCode() < PREFIX_COUNT) {
			flagCode.setCode(flagCode.getCode() + 1);
		}else {
			flagCode.setPrefix(Integer.parseInt(flagCode.getPrefix()) + 1 + "");
			flagCode.setCode(START_CODE);
		}
		codeService.updateById(flagCode);
		return convertCode(flagCode.getPrefix(), flagCode.getCode());
	}

	private static String convertCode(String prefix, Long code) {
		String name = (char)Integer.parseInt("97") + "";
		return name + code;
	}

	@Autowired
	public void setCodeService(BusCodeService codeService) {
		AccountCodeUtil.codeService = codeService;
	}
}
