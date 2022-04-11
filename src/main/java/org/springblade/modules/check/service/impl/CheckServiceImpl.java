package org.springblade.modules.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.AccessSaveCheckVO;
import org.springblade.modules.check.bean.vo.CheckApproveQualityVO;
import org.springblade.modules.check.bean.vo.CheckVO;
import org.springblade.modules.check.mapper.CheckMapper;
import org.springblade.modules.check.service.CheckService;
import org.springblade.modules.check.utils.CheckEmailUtils;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.service.BpmProcessService;
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

	@Autowired
	private BpmProcessService processService;

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
			CheckEmailUtils.sendEmail(check);
		}
		return true;
	}

	@Override
	public Boolean saveAndActiveTask(Check check) {
		boolean status = save(check);
		ApproveUtils.createTask(check.getId() + "", ApproveUtils.ApproveLinkEnum.CHECK);
		return status;
	}

	@Override
	public CheckApproveQualityVO quality() {
		LambdaQueryWrapper<BpmProcess> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcess::getAccessDept, CommonUtil.getDeptId())
			.eq(BpmProcess::getBpmServerFlag, ApproveUtils.ServerFlagEnum.CHECK_APPROVE.getMessage());

		List<BpmProcess> list = processService.list(wrapper);

		CheckApproveQualityVO result = new CheckApproveQualityVO();
		result.setAwait(0);
		result.setFinish(0);
		result.setStaleDated(0);
		for (BpmProcess process : list) {
			if (new Integer(2).equals(process.getBpmStatus())) {
				result.setAwait(result.getAwait() + 1);
			}

			if (new Integer(3).equals(process.getBpmStatus()) || new Integer(4).equals(process.getBpmStatus())) {
				result.setFinish(result.getFinish() + 1);
			}

			if (new Integer(1).equals(process.getBpmPushStatus())) {
				result.setStaleDated(result.getStaleDated() + 1);
			}
		}
		return result;
	}
}
