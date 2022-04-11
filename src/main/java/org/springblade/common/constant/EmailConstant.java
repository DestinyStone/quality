package org.springblade.common.constant;

/**
 * @Author: DestinyStone
 * @Date: 2022/4/5 15:53
 * @Description:
 */
public interface EmailConstant {
	/**
	 * 检查法制作提醒
	 */
	String INSPECTION_MAKE_REMIND = "inspection-make-remind";

	/**
	 * 工序内不良新增
	 */
	String SUBMIT_PROCESS_LOW = "submit-process-low";

	/**
	 * 工序内不良结案
	 */
	String PROCESS_LOW_COMPLETE = "process_low_complete";

	/**
	 * 工序内待审
	 */
	String PRODUCEE_LOW_AWAIT = "process_low_await";

	/**
	 * 外购件不良新增
	 */
	String SUBMIT_OUT_BUY_LOW = "submit-out-buy-low";

	/**
	 * 外购件不良结案
	 */
	String OUT_BUY_LOW_COMPLETE = "out_buy_low_complete";

	/**
	 * 新增检查法提醒
	 */
	String SUBMIT_INSPECTION = "submit-inspection";

	/**
	 * 检查法结案
	 */
	String INSPECTION_COMPLETE = "inspection_complete";

	/**
	 * di 数据上报
	 */
	String SUBMIT_DI_DATA = "submit-di-data";

	/**
	 * di上报提醒
	 */
	String DI_DATA_OVER = "di-data-over";

	/**
	 * di数据结案
	 */
	String DI_DATA_COMPLETE = "di-data-complete";
}
