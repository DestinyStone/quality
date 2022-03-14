package org.springblade.modules.work.enums;

import io.swagger.annotations.Api;

/**
 * @Author: DestinyStone
 * @Date: 2022/3/13 14:09
 * @Description:
 */
@Api("消息类型")
public enum MessageType {

	/**
	 * 已发布
	 */
	RELEASE(0, "已发布"),

	/**
	 * 已结案
	 */
	SUCCESS(0, "已结案"),

	/**
	 * 已完成
	 */
	FINISH(0, "已完成"),

	;

	Integer code;
	String message;

	MessageType(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
