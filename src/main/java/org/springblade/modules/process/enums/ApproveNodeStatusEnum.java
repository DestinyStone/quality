package org.springblade.modules.process.enums;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/30 12:08
 * @Description: 审批节点状态枚举
 */
public enum  ApproveNodeStatusEnum {

	/**
	 * 锁定
	 */
	LOCK(0, "锁定"),

	/**
	 * 未激活
	 */
	UN_ACTIVE(1, "未激活"),

	/**
	 * 已激活
	 */
	ACTIVE(2, "已激活"),

	/**
	 * 已完成
	 */
	SUCCESS(3, "已完成"),

	/**
	 * 已退回
	 */
	BACK(4, "已退回")
	;
	Integer code;
	String message;

	ApproveNodeStatusEnum(Integer code, String message) {
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
