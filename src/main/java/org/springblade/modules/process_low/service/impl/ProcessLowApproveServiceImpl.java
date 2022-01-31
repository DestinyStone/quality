package org.springblade.modules.process_low.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.process_low.bean.vo.ProcessLowApproveVO;
import org.springblade.modules.process_low.mapper.ProcessLowApproveMapper;
import org.springblade.modules.process_low.service.ProcessLowApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/30 22:27
 * @Description:
 */
@Service
public class ProcessLowApproveServiceImpl implements ProcessLowApproveService {

	@Autowired
	private ProcessLowApproveMapper lowApproveMapper;

	@Override
	public IPage<ProcessLowApproveVO> page(ProcessLowApproveVO processLowVO, Long deptId, IPage<ProcessLowApproveVO> page) {
		return page.setRecords(lowApproveMapper.page(processLowVO, deptId, page));
	}
}
