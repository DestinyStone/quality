package org.springblade.modules.process_low.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.enums.ApproveStatusEmun;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.dto.RejectDTO;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.bean.vo.ProcessLowApproveVO;
import org.springblade.modules.process_low.service.ProcessLowApproveService;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/30 22:25
 * @Description: 工序内不良审批接口
 */
@RequestMapping(RootMappingConstant.root + "/process/low/approve")
@RestController
@Api(value = "工序内不良接口", tags = "工序内不良接口")
public class ProcessLowApproveController {

	@Autowired
	private ProcessLowApproveService processLowApproveService;

	@Autowired
	private BpmProcessService processService;

	@Autowired
	private ProcessLowService lowService;

	@PostMapping("/reject")
	@ApiOperation("审批拒绝")
	public R reject(@RequestBody @Valid RejectDTO rejectDTO) {
		BpmProcess process = processService.getById(rejectDTO.getBpmId());
		if (process == null) {
			throw new ServiceException("审批不存在");
		}
		processService.reject(rejectDTO.getBpmId(), rejectDTO.getBackCause());

		ProcessLow processLow = new ProcessLow();
		processLow.setId(new Long(process.getBusId()));
		processLow.setBpmStatus(ApproveStatusEmun.BACK.getCode());
		lowService.updateById(processLow);
		return R.status(true);
	}

	@GetMapping("/page")
	@ApiOperation("分页")
	public R<IPage<ProcessLowApproveVO>> page(ProcessLowApproveVO processLowVO, Query query) {
		// 过滤出该部门超期的任务
		LambdaUpdateWrapper<BpmProcess> processWrapper = new LambdaUpdateWrapper<>();
		processWrapper.le(BpmProcess::getEndTime, new Date())
			.eq(BpmProcess::getAccessDept, CommonUtil.getDeptId())
			.set(BpmProcess::getBpmPushStatus, 1);

		processService.update(processWrapper);

		if (processLowVO.getTagFlag() == null) {
			processLowVO.setTagFlag(0);
		}
		IPage<ProcessLowApproveVO> page = processLowApproveService.page(processLowVO, CommonUtil.getDeptId(), Condition.getPage(query));
		return R.data(page);
	}
}
