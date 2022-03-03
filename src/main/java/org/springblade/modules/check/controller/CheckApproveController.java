package org.springblade.modules.check.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.cache.RoleCache;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.enums.ApproveStatusEnum;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.account.service.AccountService;
import org.springblade.modules.account.util.AccountUtils;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.CheckApproveQualityVO;
import org.springblade.modules.check.bean.vo.CheckApproveVO;
import org.springblade.modules.check.service.CheckApproveService;
import org.springblade.modules.check.service.CheckService;
import org.springblade.modules.file.bean.dto.BusFileDTO;
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
 * @Date: 2022/2/23 18:19
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/check/approve")
@RestController
@Api(value = "检查法审批接口", tags = "检查法审批接口")
public class CheckApproveController {

	@Autowired
	private BpmProcessService processService;

	@Autowired
	private CheckApproveService checkApproveService;

	@Autowired
	private ProcessUrgeService urgeService;

	@Autowired
	private CheckService checkService;

	@Autowired
	private AccountService accountService;

	@PostMapping("/reject")
	@ApiOperation("审批拒绝")
	public R reject(@RequestBody @Valid RejectDTO rejectDTO) {
		BpmProcess process = processService.getByBusId(rejectDTO.getBusId());
		processService.reject(process.getBpmId(), rejectDTO.getBackCause());

		LambdaUpdateWrapper<Check> wrapper = new LambdaUpdateWrapper<>();
		wrapper.eq(Check::getId, new Long(process.getBusId()))
			.set(Check::getBpmStatus, ApproveStatusEnum.BACK.getCode())
			.set(Check::getToyotaExcelFileId, null)
			.set(Check::getToyotaExcelFileName, null);
		checkService.update(wrapper);
		return R.status(true);
	}

	@PostMapping("/download/pass")
	@ApiOperation("下载源文件审批 通过")
	public R downloadPass(@RequestParam("id") Long id, @Valid @RequestBody BusFileDTO fileDTO) {
		BpmProcess process = processService.getByBusId(id);

		Check current = checkService.getById(id);

		// 更新文件
		Check check = new Check();
		check.setId(id);
		check.setToyotaExcelFileId(Long.parseLong(fileDTO.getId()));
		check.setToyotaExcelFileName(fileDTO.getName());
		checkService.updateById(check);

		// 审批通过
		pass(id, process.getBpmId());
		return R.data(true);
	}

	@GetMapping("/pass")
	@ApiOperation("审批通过")
	public R pass(@RequestParam("id") Long id) {
		BpmProcess process = processService.getByBusId(id);
		// 审批通过
		pass(id, process.getBpmId());
		return R.data(true);
	}

	@GetMapping("/page")
	@ApiOperation("分页")
	public R<IPage<CheckApproveVO>> page(CheckApproveVO approveVO, Query query) {
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
		IPage<CheckApproveVO> page = checkApproveService.page(approveVO, CommonUtil.getDeptId(), Condition.getPage(query));

		List<CheckApproveVO> records = page.getRecords();
		if(records.isEmpty()) {
			return R.data(page);
		}

		List<Long> bpmIds = records.stream().map(CheckApproveVO::getBpmId).collect(Collectors.toList());
		List<BpmProcessUrge> urgeList = urgeService.getByBpmIds(bpmIds);
		Map< Long, List<BpmProcessUrge>> group = urgeList.stream().collect(Collectors.groupingBy(BpmProcessUrge::getBpmId));

		ArrayList<BpmProcessUrge> defaultObject = new ArrayList<>();
		for (CheckApproveVO record : records) {
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
	public R<CheckApproveQualityVO> quality() {
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
		return R.data(result);
	}

	private void pass(Long id, Long bpmId) {
		processService.pass(bpmId);
		Check check = new Check();
		check.setId(id);
		if (processService.isProcessEnd(bpmId)) {
			Check current = checkService.getById(id);
			check.setBpmStatus(ApproveStatusEnum.FINISN.getCode());
			check.setIsAccessAccount(1);
			check.setOldToyotaExcelFileId(current.getToyotaExcelFileId());
			check.setOldToyotaExcelFileName(current.getToyotaExcelFileName());
			checkService.updateById(check);
			AccountUtils.record(check.getId(), AccountUtils.Bus.CHECK, current);
		}else {
			check.setBpmStatus(ApproveStatusEnum.PROCEED.getCode());
			checkService.updateById(check);
		}
	}
}
