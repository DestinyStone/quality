package org.springblade.modules.di.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.modules.di.bean.vo.DiReportApproveVO;
import org.springblade.modules.di.mapper.DiReportApproveMapper;
import org.springblade.modules.di.service.DiReportApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 16:10
 * @Description:
 */
@Service
public class DiReportApproveServiceImpl implements DiReportApproveService {

	@Autowired
	private DiReportApproveMapper approveMapper;

	@Override
	public void create(Long id) {
		ApproveUtils.createTask(id + "", ApproveUtils.ApproveLinkEnum.DI);
	}

	@Override
	public IPage<DiReportApproveVO> page(DiReportApproveVO vo, Long deptId, IPage<DiReportApproveVO> page) {
		return page.setRecords(approveMapper.page(vo, deptId, page));
	}
}
