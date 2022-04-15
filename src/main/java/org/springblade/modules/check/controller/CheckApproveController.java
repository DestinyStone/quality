package org.springblade.modules.check.controller;

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
import org.springblade.modules.account.service.AccountService;
import org.springblade.modules.account.util.AccountUtils;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.CheckApproveQualityVO;
import org.springblade.modules.check.bean.vo.CheckApproveVO;
import org.springblade.modules.check.service.CheckApproveService;
import org.springblade.modules.check.service.CheckService;
import org.springblade.modules.check.utils.CheckEmailUtils;
import org.springblade.modules.file.bean.dto.BusFileDTO;
import org.springblade.modules.out_buy_low.bean.dto.ResourceDTO;
import org.springblade.modules.out_buy_low.utils.ResourceConvertUtil;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.bean.BpmProcessUrge;
import org.springblade.modules.process.entity.dto.RejectDTO;
import org.springblade.modules.process.enums.ApproveNodeStatusEnum;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process.service.ProcessUrgeService;
import org.springblade.modules.system.entity.Role;
import org.springblade.modules.work.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
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

	@Autowired
	private MessageService messageService;

	private static final String messageTemp = "用户${userName} 催促品番号:${designation},品番名:${name}进行检查法制作";

	@PostMapping("/warning")
	@ApiOperation("检查制作提醒")
	public R warning(@RequestBody @Valid List<ResourceDTO> resourceDtoList) {

		List<ResourceDTO.Encumbrance> resourceList = ResourceConvertUtil.convert(resourceDtoList);
		Map<Long, ResourceDTO.Encumbrance> encumbranceMap = ResourceConvertUtil.convertMap(resourceList);

		List<Check> list = resourceList.stream().map(item -> {
			Check copy = new Check();
			copy.setResourceId(item.getResourceId());
			ResourceDTO.Encumbrance encumbrance = encumbranceMap.getOrDefault(item.getResourceId(),  new ResourceDTO.Encumbrance());
			copy.setName(encumbrance.getName());
			copy.setDesignation(encumbrance.getDesignation());
			copy.setDutyDept(encumbrance.getDutyDept());
			return copy;
		}).collect(Collectors.toList());

		for (Check check : list) {
			CheckEmailUtils.sendWarningEmail(check);
			HashMap<String, String> map = new HashMap<>();
			map.put("userName", CommonUtil.getUserName());
			map.put("designation", check.getDesignation());
			map.put("name", check.getName());
			messageService.save(CommonUtil.placeHolderReplace(messageTemp, map));
		}
		return R.status(true);
	}

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
		approveVO.setDeptId(CommonUtil.getDeptId());
		approveVO.setRoleId(CommonUtil.getRoleId());
		approveVO.setUserId(CommonUtil.getUserId());
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
		CheckApproveQualityVO result = checkService.quality();
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
