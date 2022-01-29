package org.springblade.modules.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.process.core.ProcessContainer;
import org.springblade.modules.process.entity.bean.BpmProcess;

import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/29 00:34
 * @Description:
 */
public interface BpmProcessService extends IService<BpmProcess> {

	/**
	 * 创建审批任务
	 */
	void createTask(List<ProcessContainer> list);

	/**
	 * 审批通过
	 */
	void pass(Long bpmId);
}
