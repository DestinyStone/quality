package org.springblade.modules.out_buy_low.enums;

import io.swagger.annotations.Api;

/**
 * @Author: DestinyStone
 * @Date: 2022/3/12 17:13
 * @Description:
 */
@Api("退回枚举")
public enum  RejectEnumType {

	/**
	 * 正常
	 */
	NORMAL,

	/**
	 * 供应商退回
	 */
	PROVIDER,

	/**
	 * 调查结果确认
	 */
	CHECK_CONFIRM,
}
