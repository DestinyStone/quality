package org.springblade.modules.out_buy_low.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.enums.ApproveStatusEnum;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.out_buy_low.bean.dto.OutBuyQprFillDTO;
import org.springblade.modules.out_buy_low.bean.dto.QprCheckSaveDTO;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprApproveVO;
import org.springblade.modules.out_buy_low.service.OutBuyQprApproveService;
import org.springblade.modules.out_buy_low.service.OutBuyQprService;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.bean.BpmProcessUrge;
import org.springblade.modules.process.entity.dto.RejectDTO;
import org.springblade.modules.process.enums.ApproveNodeStatusEnum;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process.service.ProcessUrgeService;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.enums.LowBpmNodeEnum;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/1 16:13
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/out/buy/qpr/approve")
@RestController
@Api(value = "工序内不良接口", tags = "工序内不良接口")
public class OutBuyQprApproveController {

	@Autowired
	private BpmProcessService processService;

	@Autowired
	private OutBuyQprApproveService outBuyQprApproveService;

	@Autowired
	private OutBuyQprService qprService;

	@Autowired
	private ProcessUrgeService urgeService;

	@Autowired
	private ProcessLowService lowService;

	@PostMapping("/handler/check/confirm/{id}")
	@ApiOperation("处理调查结果确认流程")
	public R fill(@PathVariable("id") Long id, @Valid @RequestBody OutBuyQprFillDTO fillDTO) {
		OutBuyQpr outBuyQpr = CommonUtil.copy(fillDTO, OutBuyQpr.class);
		outBuyQpr.setId(id);
		outBuyQpr.setBpmNode(LowBpmNodeEnum.CHECK_APPROVE.getCode());
		qprService.updateById(outBuyQpr);

		// 同步更新不良
		ProcessLow processLow = new ProcessLow();
		processLow.setId(id);
		processLow.setBpmNode(LowBpmNodeEnum.CHECK_APPROVE.getCode());
		lowService.updateById(processLow);

		BpmProcess process = processService.getByBusId(id);
		pass(id, process.getBpmId());
		return R.status(true);
	}

	@PostMapping("/handler/check/save/{id}")
	@ApiOperation("处理qpr录入审批")
	public R handlerCheckSave(@PathVariable("id") Long id, @Valid @RequestBody QprCheckSaveDTO saveDTO) {
		OutBuyQpr outBuyQpr = CommonUtil.copy(saveDTO, OutBuyQpr.class);
		outBuyQpr.setId(id);
		outBuyQpr.setBpmNode(LowBpmNodeEnum.CHECK_CONFIRM.getCode());
		qprService.updateById(outBuyQpr);

		// 同步更新不良
		ProcessLow processLow = new ProcessLow();
		processLow.setId(id);
		processLow.setBpmNode(LowBpmNodeEnum.CHECK_CONFIRM.getCode());
		lowService.updateById(processLow);

		BpmProcess process = processService.getByBusId(id);
		pass(id, process.getBpmId());
		return R.status(true);
	}

	@GetMapping("/pass")
	@ApiOperation("审批通过")
	public R pass(@RequestParam("id") Long id) {
		BpmProcess process = processService.getByBusId(id);
		OutBuyQpr before = qprService.getById(id);

		OutBuyQpr update = new OutBuyQpr();
		update.setId(id);
		update.setBpmNode(before.getBpmNode() + 1);
		qprService.updateById(update);
		pass(id, process.getBpmId());
		return R.status(true);
	}

	@PostMapping("/reject")
	@ApiOperation("审批拒绝")
	public R reject(@RequestBody @Valid RejectDTO rejectDTO) {
		BpmProcess process = processService.getByBusId(rejectDTO.getBusId());
		processService.reject(process.getBpmId(), rejectDTO.getBackCause());

		OutBuyQpr outBuyQpr = new OutBuyQpr();
		outBuyQpr.setId(new Long(process.getBusId()));
		outBuyQpr.setBpmStatus(ApproveStatusEnum.BACK.getCode());
		outBuyQpr.setBpmNode(-1);
		qprService.updateById(outBuyQpr);
		return R.status(true);
	}

	@GetMapping("/page")
	@ApiOperation("分页")
	public R<IPage<OutBuyQprApproveVO>> page(OutBuyQprApproveVO approveVO, Query query) {
		// 过滤出该部门超期的任务
		LambdaUpdateWrapper<BpmProcess> processWrapper = new LambdaUpdateWrapper<>();
		processWrapper.le(BpmProcess::getEndTime, new Date())
			.eq(BpmProcess::getAccessDept, CommonUtil.getDeptId())
			.eq(BpmProcess::getBpmStatus, ApproveNodeStatusEnum.ACTIVE.getCode())
			.set(BpmProcess::getBpmPushStatus, 1);

		processService.update(processWrapper);

		if (approveVO.getTagFlag() == null) {
			approveVO.setTagFlag(0);
		}
		IPage<OutBuyQprApproveVO> page = outBuyQprApproveService.page(approveVO, CommonUtil.getDeptId(), Condition.getPage(query));

		List<OutBuyQprApproveVO> records = page.getRecords();
		if(records.isEmpty()) {
			return R.data(page);
		}

		List<Long> bpmIds = records.stream().map(OutBuyQprApproveVO::getBpmId).collect(Collectors.toList());
		List<BpmProcessUrge> urgeList = urgeService.getByBpmIds(bpmIds);
		Map< Long, List<BpmProcessUrge>> group = urgeList.stream().collect(Collectors.groupingBy(BpmProcessUrge::getBpmId));

		ArrayList<BpmProcessUrge> defaultObject = new ArrayList<>();
		for (OutBuyQprApproveVO record : records) {
			List<BpmProcessUrge> urgeByBpmId = group.getOrDefault(record.getBpmId(), defaultObject);
			record.setUrgeQuality(urgeByBpmId.size());
		}

		return R.data(page);
	}

	private void pass(Long id, Long bpmId) {
		processService.pass(bpmId);
		OutBuyQpr outBuyQpr = new OutBuyQpr();
		outBuyQpr.setId(id);
		if (processService.isProcessEnd(bpmId)) {
			outBuyQpr.setBpmStatus(ApproveStatusEnum.FINISN.getCode());
			qprService.updateById(outBuyQpr);
		}else {
			outBuyQpr.setBpmStatus(ApproveStatusEnum.PROCEED.getCode());
			qprService.updateById(outBuyQpr);
		}
	}
}
