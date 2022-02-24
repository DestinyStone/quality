package org.springblade.modules.account.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.checkerframework.checker.units.qual.A;
import org.springblade.modules.account.bean.core.Version;
import org.springblade.modules.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/24 19:42
 * @Description: 台账工具类
 */
@Component
public class AccountUtils {

	private static AccountService accountService;

	/**
	 * 台账业务枚举
	 */
	public enum Bus {
		/**
		 * 检查法
		 */
		CHECK("CHECK")
		;
		String message;

		public String getMessage() {
			return message;
		}

		Bus(String message) {
			this.message = message;
		}
	}

	public static  <T extends Version> IPage<T> page(Long busId, Bus bus, IPage<T> page, Class<T> t) {
		return accountService.page(busId, bus.getMessage(), page, t);
	}

	public static  <T> void record(Long busId, Bus bus, T obj) {
		accountService.record(busId, bus.getMessage(), obj);
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		AccountUtils.accountService = accountService;
	}
}
