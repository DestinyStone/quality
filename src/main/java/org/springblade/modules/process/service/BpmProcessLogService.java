package org.springblade.modules.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.bean.BpmProcessLog;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 14:06
 * @Description:
 */
public interface BpmProcessLogService extends IService<BpmProcessLog> {
	/**
	 * 转化日志
	 * @param process
	 */
	void convertAndSave(BpmProcess process);
}
