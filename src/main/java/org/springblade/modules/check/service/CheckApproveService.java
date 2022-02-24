package org.springblade.modules.check.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.check.bean.vo.CheckApproveVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/23 18:28
 * @Description:
 */
public interface CheckApproveService {
	/**
	 * 流程分页查询
	 * @param approveVO
	 * @param deptId
	 * @param page
	 * @return
	 */
	IPage<CheckApproveVO> page(CheckApproveVO approveVO, Long deptId, IPage<CheckApproveVO> page);
}
