package org.springblade.modules.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.work.entity.bean.SettleLog;
import org.springblade.modules.work.enums.SettleBusType;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 11:21
 * @Description:
 */
public interface SettleLogService extends IService<SettleLog> {

	/**
	 * 提交日志
	 * @param title
	 * @param type
	 */
	void submitLog(String title, SettleBusType type);

	/**
	 * 结案日志
	 */
	void finishLog(String title, SettleBusType type, Long userId);

	/**
	 * 审批日志
	 */
	void processLog(String title, SettleBusType type);

	/**
	 * 拒绝日志
	 */
	void rejectLog(String title,  SettleBusType type);
}
