package org.springblade.common.enums;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/27 20:19
 * @Description:
 */
public enum  ApproveStatusEmun {
 //1审批中 3已结案 退回 4自撤回
	AWAIT(0, "待审批"),
	PROCEED(1, "1审批中"),
	FINISN(2, "已结案"),
	BACK(3, "退回"),
	SELF_BACK(4, "自测回")
	;

	private Integer code;
	private String message;

	ApproveStatusEmun(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public static ApproveStatusEmun getByCode(Integer code) {
		ApproveStatusEmun[] enumConstants = ApproveStatusEmun.class.getEnumConstants();
		for (ApproveStatusEmun item : enumConstants) {
			if (item.code.equals(code)) {
				return item;
			}
		}
		return null;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
