package org.springblade.modules.di.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.di.bean.vo.DiApproveQualityVO;
import org.springblade.modules.di.bean.vo.DiReportApproveVO;
import org.springblade.modules.di.mapper.DiReportApproveMapper;
import org.springblade.modules.di.service.DiReportApproveService;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.service.BpmProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 16:10
 * @Description:
 */
@Service
public class DiReportApproveServiceImpl implements DiReportApproveService {

	@Autowired
	private DiReportApproveMapper approveMapper;

	@Autowired
	private BpmProcessService processService;

	@Override
	public void create(Long id) {
		ApproveUtils.createTask(id + "", ApproveUtils.ApproveLinkEnum.DI);
	}

	@Override
	public IPage<DiReportApproveVO> page(DiReportApproveVO vo, Long deptId, IPage<DiReportApproveVO> page) {
		return page.setRecords(approveMapper.page(vo, deptId, page));
	}

	@Override
	public DiApproveQualityVO quality() {
		LambdaQueryWrapper<BpmProcess> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcess::getAccessDept, CommonUtil.getDeptId())
			.eq(BpmProcess::getBpmServerFlag, ApproveUtils.ServerFlagEnum.DI_APPROVE.getMessage());

		List<BpmProcess> list = processService.list(wrapper);

		DiApproveQualityVO result = new DiApproveQualityVO();
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
