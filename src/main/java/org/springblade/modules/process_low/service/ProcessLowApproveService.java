package org.springblade.modules.process_low.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.process_low.bean.vo.ProcessLowApproveVO;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/30 22:26
 * @Description:
 */
public interface ProcessLowApproveService {

	/**
	 * 自定义分页
	 * @param processLowVO
	 * @param deptId
	 * @param page
	 * @return
	 */
	IPage<ProcessLowApproveVO> page(ProcessLowApproveVO processLowVO, Long deptId, IPage<ProcessLowApproveVO> page);
}
