package org.springblade.modules.check.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.enums.ApproveStatusEnum;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CodeUtil;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.account.util.AccountUtils;
import org.springblade.modules.check.bean.dto.CheckDTO;
import org.springblade.modules.check.bean.dto.CheckResourceDTO;
import org.springblade.modules.check.bean.dto.CheckUpdateDTO;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.*;
import org.springblade.modules.check.service.CheckService;
import org.springblade.modules.check.wrapper.CheckWrapper;
import org.springblade.modules.file.bean.entity.BusFile;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.file.service.BusFileService;
import org.springblade.modules.file.wrapper.BusFileWrapper;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;
import org.springblade.modules.out_buy_low.service.OutBuyQprService;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 18:44
 * @Description: 检查法接口
 */
@RequestMapping(RootMappingConstant.root + "/check")
@RestController
@Api(value = "检查法接口", tags = "检查法接口")
public class CheckController {

	private static final String FLAG = "CHECK";

	@Autowired
	private CheckService checkService;

	@Autowired
	private ProcessLowService lowService;

	@Autowired
	private OutBuyQprService qprService;

	@Autowired
	private BusFileService fileService;

	@Autowired
	private BpmProcessService processService;

	@GetMapping("/account/version/page/{id}")
	@ApiOperation("分页")
	public R<IPage<CheckAccountVersionVO>> accountVersionPage(@PathVariable("id") Long id,  Query query) {
		return R.data(AccountUtils.page(id, AccountUtils.Bus.CHECK, Condition.getPage(query), CheckAccountVersionVO.class));
	}

	@GetMapping("/account/page")
	@ApiOperation("分页")
	public R<IPage<CheckAccountVO>> accountPage(CheckAccountVO vo, Query query) {
		LambdaUpdateWrapper<Check> wrapper = new LambdaUpdateWrapper<>();
		wrapper.and(StrUtil.isNotBlank(vo.getSearchKey()), item -> {
			item.like(Check::getName, vo.getSearchKey())
				.or()
				.like(Check::getDesignation, vo.getSearchKey())
				.or()
				.like(Check::getDutyDept, vo.getSearchKey());
		});
		wrapper.eq(Check::getIsAccessAccount, 1);

		IPage<Check> page = checkService.page(Condition.getPage(query), wrapper);

		IPage<CheckAccountVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
		voPage.setRecords(page.getRecords().stream().map(item -> {
			return CommonUtil.copy(item, CheckAccountVO.class);
		}).collect(Collectors.toList()));

		return R.data(voPage);
	}

	@GetMapping("/self/back")
	@ApiOperation("自撤回接口")
	public R selfBack(@RequestParam("id") Long id) {
		Check check = checkService.getById(id);

		// 非待审批中状态 抛出异常
		if (!ApproveStatusEnum.AWAIT.getCode().equals(check.getBpmStatus())) {
			throw new ServiceException("非待审批状态");
		}

		Check update = new Check();
		update.setId(id);
		update.setBpmStatus(ApproveStatusEnum.SELF_BACK.getCode());

		// 取消当前激活业务
		LambdaUpdateWrapper<BpmProcess> processUpdateWrapper = new LambdaUpdateWrapper<>();
		processUpdateWrapper.eq(BpmProcess::getBusId, id)
			.eq(BpmProcess::getIsCastoff, 0)
			.set(BpmProcess::getBpmStatus, 0);

		// 删除该任务
		processService.update(processUpdateWrapper);
		return R.status(checkService.updateById(update));
	}

	@GetMapping("/detail")
	@ApiOperation("详情")
	public R<CheckDetailVO> detail(@RequestParam("id") Long id) {
		Check check = checkService.getById(id);
		CheckDetailVO detail = CheckWrapper.build().entityVO(check);

		ArrayList<Long> fileIds = new ArrayList<>();
		Long changeImageId = detail.getChangeImageId();
		Long providerExcelFileId = detail.getProviderExcelFileId();
		Long providerSignatureId = detail.getProviderSignatureId();
		Long toyotaExcelFileId = detail.getToyotaExcelFileId();
		List<Long> extendsFileIds = CommonUtil.toLongList(detail.getExtendsFileIds());

		fileIds.addAll(Arrays.asList(changeImageId, providerExcelFileId, providerSignatureId));
		if (toyotaExcelFileId != null) {
			fileIds.add(toyotaExcelFileId);
		}
		if (!extendsFileIds.isEmpty()) {
			fileIds.addAll(extendsFileIds);
		}

		if (fileIds.isEmpty()) {
			return R.data(detail);
		}

		LambdaQueryWrapper<BusFile> fileWrapper = new LambdaQueryWrapper<>();
		fileWrapper.in(BusFile::getId, fileIds);
		List<BusFileVO> list = BusFileWrapper.build().listVO(fileService.list(fileWrapper));

		detail.setExtendsFiles(new ArrayList<>());

		for (BusFileVO item : list) {
			if(extendsFileIds.contains(item.getId())) {
				detail.getExtendsFiles().add(item);
			}
			if (item.getId().equals(changeImageId)) {
				detail.setChangeImageFile(item);
				detail.setChangeImageUrl(item.getUrl());
			}

			if (item.getId().equals(providerExcelFileId)) {
				detail.setProviderExcelFile(item);
			}

			if (item.getId().equals(providerSignatureId)) {
				detail.setProviderSignatureFile(item);
			}

			if (item.getId().equals(toyotaExcelFileId)) {
				detail.setToyotaExcelFile(item);
			}
		}

		return R.data(detail);
	}

	@PostMapping("/update/{id}")
	@ApiOperation("更新")
	public R save(@PathVariable("id") Long id, @Valid @RequestBody CheckUpdateDTO checkDTO) {
		Check current = checkService.getById(id);
		if (!(ApproveStatusEnum.FINISN.getCode().equals(current.getBpmStatus()) || ApproveStatusEnum.BACK.getCode().equals(current.getBpmStatus()))) {
			throw new ServiceException("当前流程审批中");
		}

		Check check = CommonUtil.copy(checkDTO, Check.class);
		check.setId(id);
		check.setBpmNode(1);
		check.setUpdateTime(new Date());
		check.setBpmStatus(ApproveStatusEnum.AWAIT.getCode());
		check.setUpdateUser(CommonUtil.getUserId());
		checkService.updateById(check);
		LambdaUpdateWrapper<Check> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(Check::getId, id)
			.set(Check::getBpmNode, 1)
			.set(Check::getToyotaExcelFileName, null)
			.set(Check::getToyotaExcelFileId, null);
		boolean status = checkService.update(updateWrapper);

		// 废除之前的审批任务
		LambdaUpdateWrapper<BpmProcess> wrapper = new LambdaUpdateWrapper<>();
		wrapper.eq(BpmProcess::getBusId, id)
			.set(BpmProcess::getIsCastoff, 1);
		processService.update(wrapper);

		// 开启 审批
		ApproveUtils.createTask(id + "", ApproveUtils.ApproveLinkEnum.CHECK);
		return R.data(status);
	}

	@PostMapping("/save")
	@ApiOperation("新增")
	@Transactional
	public R save(@Valid @RequestBody CheckDTO checkDTO) {

		List<CheckResourceDTO> resourceList = checkDTO.getResourceList();
		List<CheckResourceDTO.Encumbrance> encumbranceList = new ArrayList<>();
		List<Long> qprIds = resourceList.stream().filter(item -> item.getResourceType().equals(0)).map(CheckResourceDTO::getResourceId).collect(Collectors.toList());
		if (!qprIds.isEmpty()) {
			List<CheckResourceDTO.Encumbrance> collect = qprService.getByIds(qprIds).stream().map(item -> {
				CheckResourceDTO.Encumbrance encumbrance = new CheckResourceDTO.Encumbrance();
				encumbrance.setResourceId(item.getId());
				encumbrance.setDesignation(item.getDesignation());
				encumbrance.setName(item.getName());
				encumbrance.setDutyDept(item.getDutyDept());
				return encumbrance;
			}).collect(Collectors.toList());
			encumbranceList.addAll(collect);

			LambdaUpdateWrapper<OutBuyQpr> wrapper = new LambdaUpdateWrapper<>();
			wrapper.in(OutBuyQpr::getId, qprIds)
				.set(OutBuyQpr::getIsAccessCheck, 0);
			qprService.update(wrapper);
		}

		List<Long> lowIds = resourceList.stream().filter(item -> item.getResourceType().equals(1)).map(CheckResourceDTO::getResourceId).collect(Collectors.toList());
		if (!lowIds.isEmpty()) {
			List<CheckResourceDTO.Encumbrance> collect = lowService.getByIds(lowIds).stream().map(item -> {
				CheckResourceDTO.Encumbrance encumbrance = new CheckResourceDTO.Encumbrance();
				encumbrance.setResourceId(item.getId());
				encumbrance.setDesignation(item.getDesignation());
				encumbrance.setName(item.getName());
				encumbrance.setDutyDept(item.getDutyDept());
				return encumbrance;
			}).collect(Collectors.toList());
			encumbranceList.addAll(collect);

			LambdaUpdateWrapper<ProcessLow> wrapper = new LambdaUpdateWrapper<>();
			wrapper.in(ProcessLow::getId, lowIds)
				.set(ProcessLow::getIsAccessCheck, 0);
			lowService.update(wrapper);
		}

		Map<Long, CheckResourceDTO.Encumbrance> encumbranceMap = encumbranceList.stream().collect(Collectors.toMap(CheckResourceDTO.Encumbrance::getResourceId, Function.identity()));


		List<Check> collect = resourceList.stream().map(item -> {
			Check copy = CommonUtil.copy(checkDTO, Check.class);
			copy.setCode(CodeUtil.getCode(FLAG));
			copy.setResourceId(item.getResourceId());

			CheckResourceDTO.Encumbrance encumbrance = encumbranceMap.getOrDefault(item.getResourceId(),  new CheckResourceDTO.Encumbrance());
			copy.setName(encumbrance.getName());
			copy.setDesignation(encumbrance.getDesignation());
			copy.setDutyDept(encumbrance.getDutyDept());

			copy.setResourceType(item.getResourceType());
			copy.setBpmNode(0);
			copy.setIsAccessAccount(0);
			copy.setBpmStatus(ApproveStatusEnum.AWAIT.getCode());
			copy.setCreateUser(CommonUtil.getUserId());
			copy.setCreateDept(CommonUtil.getDeptId());
			copy.setCreateTime(new Date());
			copy.setUpdateUser(CommonUtil.getUserId());
			copy.setUpdateTime(new Date());
			return copy;
		}).collect(Collectors.toList());

		return R.status(checkService.saveBatchAndActiveTask(collect));
	}

	@GetMapping("/access/save/page")
	@ApiOperation("运行新增的检查法分页")
	public R<IPage<AccessSaveCheckVO>> page(AccessSaveCheckVO approveVO, Query query) {
		approveVO.setDeptId(CommonUtil.getDeptId());
		IPage<AccessSaveCheckVO> page = checkService.accessSavePage(approveVO, Condition.getPage(query));
		return R.data(page);
	}

	@GetMapping("/page")
	@ApiOperation("分页")
	public R<IPage<CheckVO>> page(CheckVO checkVO, Query query) {
		LambdaQueryWrapper<Check> wrapper = new LambdaQueryWrapper<>();
		wrapper.and(StrUtil.isNotBlank(checkVO.getSearchKey()), item -> {
			item.like(Check::getName, checkVO.getSearchKey())
				.or()
				.like(Check::getDesignation, checkVO.getSearchKey())
				.or()
				.like(Check::getDutyDept, checkVO.getSearchKey());
		});

		if (!new Integer(-1).equals(checkVO.getBpmStatusFilter())) {
			// 自测回
			if (new Integer(0).equals(checkVO.getBpmStatusFilter())) {
				wrapper.eq(Check::getBpmStatus, ApproveStatusEnum.SELF_BACK.getCode());
			}

			// 已驳回
			if (new Integer(1).equals(checkVO.getBpmStatusFilter())) {
				wrapper.eq(Check::getBpmStatus, ApproveStatusEnum.BACK.getCode());
			}

			// 进行中
			if (new Integer(2).equals(checkVO.getBpmStatusFilter())) {
				wrapper.and(item -> {
					item.eq(Check::getBpmStatus, ApproveStatusEnum.AWAIT.getCode())
						.or()
						.eq(Check::getBpmStatus, ApproveStatusEnum.PROCEED.getCode());
				});
			}

			// 已办结
			if (new Integer(3).equals(checkVO.getBpmStatusFilter())) {
				wrapper.eq(Check::getBpmStatus, ApproveStatusEnum.FINISN.getCode());
			}
		}
		IPage<Check> page = checkService.page(Condition.getPage(query), wrapper);

		IPage<CheckVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
		voPage.setRecords(page.getRecords().stream().map(item -> {
			return CommonUtil.copy(item, CheckVO.class);
		}).collect(Collectors.toList()));

		return R.data(voPage);
	}

	@GetMapping("/quality")
	@ApiOperation("统计接口")
	public R<CheckQualityVO> quality() {
		LambdaQueryWrapper<Check> wrapper = new LambdaQueryWrapper<>();
		List<Check> list = checkService.list(wrapper);

		CheckQualityVO qualityVO = new CheckQualityVO();
		qualityVO.setBack(0);
		qualityVO.setFinish(0);
		qualityVO.setProcess(0);
		qualityVO.setSelfBack(0);

		for (Check item : list) {
			if (item.getBpmStatus().equals(ApproveStatusEnum.SELF_BACK.getCode())) {
				qualityVO.setSelfBack(qualityVO.getSelfBack() + 1);
			}
			if (item.getBpmStatus().equals(ApproveStatusEnum.BACK.getCode())) {
				qualityVO.setBack(qualityVO.getBack() + 1);
			}
			if (item.getBpmStatus().equals(ApproveStatusEnum.AWAIT.getCode()) ||
				item.getBpmStatus().equals(ApproveStatusEnum.PROCEED.getCode())) {
				qualityVO.setProcess(qualityVO.getProcess() + 1);
			}
			if (item.getBpmStatus().equals(ApproveStatusEnum.FINISN.getCode())) {
				qualityVO.setFinish(qualityVO.getFinish() + 1);
			}
		}
		return R.data(qualityVO);
	}

}

