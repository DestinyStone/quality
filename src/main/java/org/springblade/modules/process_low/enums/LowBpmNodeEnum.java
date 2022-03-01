package org.springblade.modules.process_low.enums;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/1 19:15
 * @Description: 不良审批节点
 */
public enum LowBpmNodeEnum {
	QPR_SAVE(0, "不良联络书发行确认"),
	QPR_APPROVE(1, "不良联络书发行审批"),
	CHECK_SAVE(2, "不良调查"),
	CHECK_CONFIRM(3, "调查结果确认"),
	CHECK_APPROVE(4, "调查结果审批")
	;
	Integer code;
	private String message;

	LowBpmNodeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public static LowBpmNodeEnum getByCode(Integer code) {
		LowBpmNodeEnum[] enumConstants = LowBpmNodeEnum.class.getEnumConstants();
		for (LowBpmNodeEnum enumConstant : enumConstants) {
			if (enumConstant.code.equals(code)) {
				return enumConstant;
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
