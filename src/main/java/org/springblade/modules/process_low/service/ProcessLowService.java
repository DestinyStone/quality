package org.springblade.modules.process_low.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.process.entity.dto.RejectDTO;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.bean.vo.ProcessLowApproveQualityVO;

import java.util.List;

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

	/**
	 * ids查询
	 * @param qprIds
	 * @return
	 */
	List<ProcessLow> getByIds(List<Long> qprIds);

	/**
	 * qpr 拒绝
	 * @param rejectDTO
	 */
	void handlerQprReject(RejectDTO rejectDTO);

	/**
	 * 统计
	 * @return
	 */
	ProcessLowApproveQualityVO quality();
}
