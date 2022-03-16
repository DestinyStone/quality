package org.springblade.common.enums;

/**
 * @Author: DestinyStone
 * @Date: 2022/3/16 12:50
 * @Description:
 */
public enum  EmailType {
	/**
	 * 腾讯
	 */
	QQ(0, "腾讯", "2777679537@qq.com"),
	;
	Integer code;
	String message;
	String from;

	EmailType(Integer code, String message, String from) {
		this.code = code;
		this.message = message;
		this.from = from;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getFrom() {
		return from;
	}
}
