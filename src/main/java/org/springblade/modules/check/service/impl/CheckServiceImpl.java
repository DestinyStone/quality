package org.springblade.modules.check.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.AccessSaveCheckVO;
import org.springblade.modules.check.bean.vo.CheckVO;
import org.springblade.modules.check.mapper.CheckMapper;
import org.springblade.modules.check.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 18:51
 * @Description:
 */
@Service
public class CheckServiceImpl extends ServiceImpl<CheckMapper, Check> implements CheckService {

	@Autowired
	private CheckMapper checkMapper;

	@Override
	public IPage<AccessSaveCheckVO> accessSavePage(AccessSaveCheckVO approveVO, IPage<AccessSaveCheckVO> page) {
		return page.setRecords(checkMapper.accessSavePage(approveVO, page));
	}

	@Override
	public IPage<CheckVO> customPage(CheckVO checkVO, IPage<CheckVO> page) {
		return page.setRecords(checkMapper.page(checkVO, page));
	}

	@Override
	@Transactional
	public Boolean saveBatchAndActiveTask(List<Check> collect) {
		if (collect.isEmpty()) {
			return false;
		}
		for (Check check : collect) {
			saveAndActiveTask(check);
		}
		return true;
	}

	@Override
	public Boolean saveAndActiveTask(Check check) {
		boolean status = save(check);
		ApproveUtils.createTask(check.getId() + "", ApproveUtils.ApproveLinkEnum.CHECK);
		return status;
	}
}
