package org.springblade.common.enums;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/9 13:50
 * @Description:
 */
public enum  ApproveOperatorEnum {

	/**
	 * 提交申请
	 */
	COMMIT(0, "提交申请"),

	/**
	 * 回应
	 */
	RESPONSE(1, "回应"),

	/**
	 * 审批
	 */
	PASS(2, "审批")
	;

	private Integer code;
	private String message;

	public Integer getCode() {
		return code;
	}

	ApproveOperatorEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
