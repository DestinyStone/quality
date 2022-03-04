package org.springblade.modules.di.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.di.bean.vo.DiReportApproveVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 16:10
 * @Description:
 */
public interface DiReportApproveService {
	/**
	 * 创建审批任务
	 * @param id
	 */
	void create(Long id);

	/**
	 * 审批分页
	 * @param vo
	 * @param deptId
	 * @param page
	 * @return
	 */
	IPage<DiReportApproveVO> page(DiReportApproveVO vo, Long deptId, IPage<DiReportApproveVO> page);
}
