package org.springblade.modules.process_low.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.process_low.bean.entity.ProcessLow;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 11:23
 * @Description:
 */
public interface ProcessLowService extends IService<ProcessLow> {
	/**
	 * 新增并激活审批任务
	 * @param processLow
	 * @return
	 */
	Boolean saveAndActiveTask(ProcessLow processLow);
}
