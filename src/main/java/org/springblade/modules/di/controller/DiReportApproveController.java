package org.springblade.modules.di.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.cache.RoleCache;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.enums.ApproveStatusEnum;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.di.bean.dto.DiReportTaskSubmitDTO;
import org.springblade.modules.di.bean.entity.DiReport;
import org.springblade.modules.di.bean.vo.DiApproveQualityVO;
import org.springblade.modules.di.bean.vo.DiReportApproveVO;
import org.springblade.modules.di.service.DiReportApproveService;
import org.springblade.modules.di.service.DiReportService;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.bean.BpmProcessUrge;
import org.springblade.modules.process.entity.dto.RejectDTO;
import org.springblade.modules.process.enums.ApproveNodeStatusEnum;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process.service.ProcessUrgeService;
import org.springblade.modules.system.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 16:10
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/di/approve")
@RestController
@Api(value = "Di审批接口", tags = "Di审批接口")
public class DiReportApproveController {

	@Autowired
	private BpmProcessService processService;

	@Autowired
	private DiReportApproveService diApproveService;

	@Autowired
	private ProcessUrgeService urgeService;

	@Autowired
	private DiReportService reportService;

	@PostMapping("/reject")
	@ApiOperation("审批拒绝")
	public R reject(@RequestBody @Valid RejectDTO rejectDTO) {
		BpmProcess process = processService.getByBusId(rejectDTO.getBusId());
		processService.reject(process.getBpmId(), rejectDTO.getBackCause());

		LambdaUpdateWrapper<DiReport> wrapper = new LambdaUpdateWrapper<>();
		wrapper.eq(DiReport::getId, new Long(process.getBusId()))
			.set(DiReport::getBpmStatus, ApproveStatusEnum.BACK.getCode())
			.set(DiReport::getToyoDataFileId, null)
			.set(DiReport::getToyoDataFileName, null);
		reportService.update(wrapper);
		return R.status(true);
	}

	@PostMapping("/task/pass/{id}")
	@ApiOperation("担当审批")
	public R taskApprove(@PathVariable("id") Long id, @RequestBody @Valid DiReportTaskSubmitDTO submitDTO) {
		DiReport report = new DiReport();
		report.setId(id);
		report.setBpmStatus(ApproveStatusEnum.PROCEED.getCode());
		report.setToyoDataFileId(submitDTO.getToyoDataFileId());
		report.setToyoDataFileName(submitDTO.getToyoDataFileName());
		reportService.updateById(report);

		BpmProcess process = processService.getByBusId(id);
		pass(report.getId(), process.getBpmId());
		return R.status(true);
	}

	@GetMapping("/pass")
	@ApiOperation("担当审批")
	public R pass(@RequestParam("id") Long id) {
		BpmProcess process = processService.getByBusId(id);
		pass(id, process.getBpmId());
		return R.status(true);
	}


	@GetMapping("/page")
	@ApiOperation("分页")
	public R<IPage<DiReportApproveVO>> page(DiReportApproveVO vo, Query query) {
		// 过滤出该部门超期的任务
		LambdaUpdateWrapper<BpmProcess> processWrapper = new LambdaUpdateWrapper<>();
		processWrapper.le(BpmProcess::getEndTime, new Date())
			.eq(BpmProcess::getAccessDept, CommonUtil.getDeptId())
			.eq(BpmProcess::getBpmStatus, ApproveNodeStatusEnum.ACTIVE.getCode())
			.set(BpmProcess::getBpmPushStatus, 1);

		processService.update(processWrapper);

		if (vo.getTagFlag() == null) {
			vo.setTagFlag(0);
		}
		IPage<DiReportApproveVO> page = diApproveService.page(vo, CommonUtil.getDeptId(), Condition.getPage(query));

		List<DiReportApproveVO> records = page.getRecords();
		if(records.isEmpty()) {
			return R.data(page);
		}

		List<Long> bpmIds = records.stream().map(DiReportApproveVO::getBpmId).collect(Collectors.toList());
		List<BpmProcessUrge> urgeList = urgeService.getByBpmIds(bpmIds);
		Map< Long, List<BpmProcessUrge>> group = urgeList.stream().collect(Collectors.groupingBy(BpmProcessUrge::getBpmId));

		ArrayList<BpmProcessUrge> defaultObject = new ArrayList<>();
		for (DiReportApproveVO record : records) {
			List<BpmProcessUrge> urgeByBpmId = group.getOrDefault(record.getBpmId(), defaultObject);
			record.setUrgeQuality(urgeByBpmId.size());

			Role role = RoleCache.getRole(Long.parseLong(record.getDutyDept()));
			if (role != null) {
				record.setDutyDept(role.getRoleName());
			}
		}

		return R.data(page);
	}

	@GetMapping("/quality")
	@ApiOperation("统计")
	public R<DiApproveQualityVO> quality() {
		DiApproveQualityVO result = diApproveService.quality();
		return R.data(result);
	}

	private void pass(Long id, Long bpmId) {
		processService.pass(bpmId);
		DiReport report = new DiReport();
		report.setId(id);
		if (processService.isProcessEnd(bpmId)) {
			report.setBpmStatus(ApproveStatusEnum.FINISN.getCode());
			reportService.updateById(report);
			reportService.updateConfig(id);
		}
	}
}
