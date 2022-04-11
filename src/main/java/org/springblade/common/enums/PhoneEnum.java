package org.springblade.common.enums;

/**
 * @Author: xiaoxia
 * @Date: 2022/4/8 11:06
 * @Description:
 */
public class PhoneEnum {
	public static enum Single {
		MAIN(""),
		;
		String message;

		Single(String message) {
			this.message = message;
		}
	}

	public static enum TEMPLATE {
		TEST("SMS_237585080"),
		;

		String message;

		TEMPLATE(String message) {
			this.message = message;
		}
	}
}
