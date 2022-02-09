package org.springblade.common.enums;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/9 13:38
 * @Description:
 */
public enum  ApproveNodeEnum {

	/**
	 * 用户已提交
	 */
	COMMIT( "commit", ApproveOperatorEnum.COMMIT),

	/**
	 * QPR 提交
	 */
	QPR_SAVE("qprSave", ApproveOperatorEnum.COMMIT),

	/**
	 * 不良联络书发行审批
	 */
	QPR_APPROVE( "qprApprove", ApproveOperatorEnum.PASS),

	/**
	 * 调查结果录入
	 */
	CHECK_SAVE("checkSave", ApproveOperatorEnum.RESPONSE),

	/**
	 * 调查结果确认
	 */
	CHECK_CONFIRM("checkConfirm", ApproveOperatorEnum.PASS),

	/**
	 * 调查结果审批
	 */
	CHECK_APPROVE("checkApprove", ApproveOperatorEnum.PASS),
	/**
	 * 工序内不良审批
	 */
	PROCESS_LOW_APPROVE("processLowApprove", ApproveOperatorEnum.PASS),
	;

	private String message;
	private ApproveOperatorEnum operatorEnum;

	ApproveNodeEnum(String message, ApproveOperatorEnum operatorEnum) {
		this.message = message;
		this.operatorEnum = operatorEnum;
	}

	public static ApproveNodeEnum getByMessage(String message) {
		ApproveNodeEnum[] enumConstants = ApproveNodeEnum.class.getEnumConstants();
		for (ApproveNodeEnum enumConstant : enumConstants) {
			if (enumConstant.message.equals(message)) {
				return enumConstant;
			}
		}
		return null;
	}

	public String getMessage() {
		return message;
	}

	public ApproveOperatorEnum getOperatorEnum() {
		return operatorEnum;
	}
}
