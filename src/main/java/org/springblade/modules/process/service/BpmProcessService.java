package org.springblade.modules.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.out_buy_low.enums.RejectEnumType;
import org.springblade.modules.process.core.ProcessContainer;
import org.springblade.modules.process.entity.bean.BpmProcess;

import java.util.List;
import java.util.Map;

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

	/**
	 * 审批通过
	 * @param bpmId
	 * @param map
	 */
	void pass(Long bpmId, Map<String, String> map);

	/**
	 * 审批拒绝
	 */
	void reject(Long bpmId, String cause);

	/**
	 * 审批拒绝
	 */
	void reject(Long bpmId, String backCause, RejectEnumType provider);

	/**
	 * 审批是否已完成
	 * @param bpmId
	 * @return false 未结束 true 已结束
	 */
	Boolean isProcessEnd(Long bpmId);

	/**
	 * 删除该任务
	 * @param busId  业务Id
	 */
	void delete(Long busId);

	/**
	 * 根据bugId 获取
	 * @param id
	 * @return
	 */
	BpmProcess getByBusId(Long id);

}
