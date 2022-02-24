package org.springblade.modules.check.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.check.bean.vo.CheckApproveVO;
import org.springblade.modules.check.mapper.CheckApproveMapper;
import org.springblade.modules.check.service.CheckApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/23 18:29
 * @Description:
 */
@Service
public class CheckApproveServiceImpl implements CheckApproveService {

	@Autowired
	private CheckApproveMapper approveMapper;

	@Override
	public IPage<CheckApproveVO> page(CheckApproveVO approveVO, Long deptId, IPage<CheckApproveVO> page) {
		return page.setRecords(approveMapper.page(approveVO, deptId, page));
	}
}
