package org.springblade.modules.process_low.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.out_buy_low.bean.dto.OutBuyQprDTO;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;
import org.springblade.modules.out_buy_low.service.OutBuyQprService;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.dto.RejectDTO;
import org.springblade.modules.process.enums.ApproveNodeStatusEnum;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.bean.vo.ProcessLowApproveQualityVO;
import org.springblade.modules.process_low.bean.vo.ProcessLowApproveVO;
import org.springblade.modules.process_low.service.ProcessLowApproveService;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

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

	@Autowired
	private OutBuyQprService qprService;

	@PostMapping("/handler/qpr/save/{id}")
	@ApiOperation("处理qpr录入审批")
	public R handlerQprSave(@PathVariable("id") Long id,
							@RequestBody @Valid OutBuyQprDTO qprDTO) {
		BpmProcess process = processService.getByBusId(id);

		OutBuyQpr qpr = BeanUtil.copy(qprDTO, OutBuyQpr.class);
		qpr.setId(id);
		Boolean status = qprService.saveUnActiveTask(qpr);

		processService.pass(process.getBpmId());
		return R.status(status);

	}

	@GetMapping("/pass")
	@ApiOperation("审批通过")
	public R pass(@RequestParam("id") Long id) {
		LambdaQueryWrapper<BpmProcess> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcess::getBpmStatus, ApproveNodeStatusEnum.ACTIVE.getCode())
			.eq(BpmProcess::getBusId, id);
		BpmProcess process = processService.getOne(wrapper);

		if(process == null) {
			throw new ServiceException("当前审批节点不存在");
		}
		processService.pass(process.getBpmId());
		if (processService.isProcessEnd(process.getBpmId())) {
			ProcessLow processLow = new ProcessLow();
			processLow.setId(id);
			processLow.setBpmStatus(ApproveStatusEmun.FINISN.getCode());
			lowService.updateById(processLow);
		}
		return R.status(true);
	}

	@PostMapping("/reject")
	@ApiOperation("审批拒绝")
	public R reject(@RequestBody @Valid RejectDTO rejectDTO) {
		BpmProcess process = processService.getByBusId(rejectDTO.getBusId());
		processService.reject(process.getBpmId(), rejectDTO.getBackCause());

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

	@GetMapping("/quality")
	@ApiOperation("统计")
	public R<ProcessLowApproveQualityVO> quality() {
		LambdaQueryWrapper<BpmProcess> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcess::getAccessDept, CommonUtil.getDeptId());

		List<BpmProcess> list = processService.list(wrapper);

		ProcessLowApproveQualityVO result = new ProcessLowApproveQualityVO();
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
		return R.data(result);
	}
}
