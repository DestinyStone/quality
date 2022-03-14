package org.springblade.modules.work.enums;

import io.swagger.annotations.Api;

/**
 * @Author: DestinyStone
 * @Date: 2022/3/13 14:09
 * @Description:
 */
@Api("消息业务类型")
public enum SettleBusType {

	/**
	 * 工序内不良
	 */
	LOW(0, "工序内不良"),

	/**
	 * 外购件不良
	 */
	OUT_LOW(1, "外购件不良"),

	/**
	 * 检查法
	 */
	CHECK(2, "检查法"),

	/**
	 * 检查法
	 */
	DI(3, "检查法"),
	;

	Integer code;
	String message;

	SettleBusType(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
