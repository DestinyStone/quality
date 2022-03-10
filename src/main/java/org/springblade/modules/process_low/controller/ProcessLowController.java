package org.springblade.modules.process_low.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.file.bean.entity.BusFile;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.file.service.BusFileService;
import org.springblade.modules.file.wrapper.BusFileWrapper;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;
import org.springblade.modules.out_buy_low.service.OutBuyQprService;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process_low.bean.dto.ProcessLowAdviceUploadDTO;
import org.springblade.modules.process_low.bean.dto.ProcessLowCheckDTO;
import org.springblade.modules.process_low.bean.dto.ProcessLowDTO;
import org.springblade.modules.process_low.bean.dto.ProcessLowStandardUploadDTO;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.bean.vo.ProcessLowQualityVO;
import org.springblade.modules.process_low.bean.vo.ProcessLowVO;
import org.springblade.modules.process_low.enums.LowBpmNodeEnum;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springblade.modules.process_low.utils.ProcessLowExcelUtil;
import org.springblade.modules.process_low.wrapper.ProcessLowWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 11:26
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/process/low")
@RestController
@Api(value = "工序内不良接口", tags = "工序内不良接口")
public class ProcessLowController {

	private static final String CODE_FLAG = "LOW";

	@Autowired
	private ProcessLowService lowService;

	@Autowired
	private OutBuyQprService qprService;

	@Autowired
	private BusFileService fileService;

	@Autowired
	private BpmProcessService processService;

	@PostMapping("/account/upload/advice/{id}")
	public R uploadAdvice(@PathVariable("id") Long id, @Valid @RequestBody ProcessLowAdviceUploadDTO uploadDTO) {
		ProcessLow processLow = lowService.getById(id);
		if (ApproveStatusEnum.FINISN.getCode().equals(processLow.getBpmStatus())) {
			throw new ServiceException("已结案 禁止提交");
		}
		ProcessLow upload = new ProcessLow();
		upload.setId(id);
		upload.setUpdateUser(AuthUtil.getUserId());
		upload.setUpdateTime(new Date());

		if (!new Integer("1").equals(processLow.getIsBusinessFile())) {
			throw new ServiceException("禁止上传业务通知书");
		}

		if (uploadDTO.getBusinessAdviceFileId() != null) {
			upload.setBusinessAdviceFileId(uploadDTO.getBusinessAdviceFileId());
			upload.setBusinessAdviceFileName(uploadDTO.getBusinessAdviceFileName());
			upload.setPassAdviceFileId(uploadDTO.getPassAdviceFileId());
			upload.setPassAdviceFileName(uploadDTO.getPassAdviceFileName());
		}

		if (uploadDTO.getPassAdviceFileId() != null) {
			upload.setPassAdviceFileId(uploadDTO.getPassAdviceFileId());
			upload.setPassAdviceFileName(uploadDTO.getPassAdviceFileName());
		}

		return R.data(lowService.updateById(upload));
	}

	@PostMapping("/account/upload/standard/{id}")
	public R uploadStandard(@PathVariable("id") Long id, @Valid @RequestBody ProcessLowStandardUploadDTO uploadDTO) {
		ProcessLow processLow = lowService.getById(id);
		if (ApproveStatusEnum.FINISN.getCode().equals(processLow.getBpmStatus())) {
			throw new ServiceException("已结案 禁止提交");
		}
		ProcessLow upload = new ProcessLow();
		upload.setId(id);
		upload.setUpdateUser(AuthUtil.getUserId());
		upload.setUpdateTime(new Date());

		if (!new Integer("1").equals(processLow.getIsUploadStandardFile())) {
			throw new ServiceException("禁止上传标准类通知书");
		}
		upload.setStandardFileId(uploadDTO.getStandardFileId());
		upload.setStandardFileName(uploadDTO.getStandardFileName());
		return R.data(lowService.updateById(upload));
	}

	@GetMapping("/account/export")
	@ApiOperation("分页")
	public void export(ProcessLowVO processLowVO, Query query, HttpServletResponse response) {
		R<IPage<ProcessLowVO>> page = accountPage(processLowVO, query);
		ProcessLowExcelUtil.export(page.getData().getRecords(), response);
	}

	@GetMapping("/account/page")
	@ApiOperation("分页")
	public R<IPage<ProcessLowVO>> accountPage(ProcessLowVO processLowVO, Query query) {
		R<IPage<ProcessLowVO>> page = page(processLowVO, query);
		List<ProcessLowVO> records = page.getData().getRecords();

		List<Long> ids = records.isEmpty() ? new ArrayList<>() : records.stream().map(ProcessLowVO::getId).collect(Collectors.toList());
		List<Long> fileIds =  records.isEmpty() ? new ArrayList<>() :records.stream().flatMap(item -> {
			return CommonUtil.toLongList(item.getImgReportIds()).stream();
		}).collect(Collectors.toList());
		Map<Long, BusFileVO> fileMap = fileIds.isEmpty() ? new HashMap<>() : fileService.getByIds(fileIds).stream().collect(Collectors.toMap(BusFileVO::getId, Function.identity()));

		Map<Long, OutBuyQpr> qprMap = ids.isEmpty() ? new HashMap<>() : qprService.getByIds(ids).stream().collect(Collectors.toMap(OutBuyQpr::getId, Function.identity()));
		for (ProcessLowVO record : records) {
			OutBuyQpr outBuyQpr = qprMap.get(record.getId());
			if (outBuyQpr != null) {
				record.setAnalyseTriggerCause(outBuyQpr.getAnalyseTriggerCause());
				record.setAnalyseOutCause(outBuyQpr.getAnalyseOutCause());
				record.setAnalyseTriggerStrategy(outBuyQpr.getAnalyseTriggerStrategy());
				record.setAnalyseOutStrategy(outBuyQpr.getAnalyseOutStrategy());
			}

			if (StrUtil.isNotBlank(record.getImgReportIds())) {
				record.setImgReportList(new ArrayList<>());
				for (Long fileId : CommonUtil.toLongList(record.getImgReportIds())) {
					record.getImgReportList().add(fileMap.getOrDefault(fileId, new BusFileVO()));
				}
			}
		}


		return page;
	}

	@PostMapping("/re/submit/{id}")
	@ApiOperation("重新提交")
	@Transactional
	public R reSubmit(@PathVariable("id") Long id, @Valid @RequestBody ProcessLowDTO processLowDTO) {
		ProcessLow processLow = BeanUtil.copy(processLowDTO, ProcessLow.class);
		processLow.setId(id);
		processLow.setUpdateUser(CommonUtil.getUserId());
		processLow.setUpdateTime(new Date());
		processLow.setBpmStatus(ApproveStatusEnum.AWAIT.getCode());
		if (processLow.getType().equals(0)) {
			processLow.setBpmNode(LowBpmNodeEnum.QPR_SAVE.getCode());
		}else {
			processLow.setBpmNode(LowBpmNodeEnum.CHECK_SAVE.getCode());
		}

		Boolean status = lowService.updateById(processLow);

			// 废除之前的审批任务
		LambdaUpdateWrapper<BpmProcess> wrapper = new LambdaUpdateWrapper<>();
		wrapper.eq(BpmProcess::getBusId, id)
			.set(BpmProcess::getIsCastoff, 1);
		processService.update(wrapper);

		if (new Integer(0).equals(processLow.getType())) {
			// 开启审批流程
			ApproveUtils.createTask(processLow.getId() + "", ApproveUtils.ApproveLinkEnum.PROCESS_LOW_OUT_BUY);
		}else {
			// 开启 工序内不良 审批
			ApproveUtils.createTask(processLow.getId() + "", ApproveUtils.ApproveLinkEnum.PROCESS_LOW);
		}

		return R.data(status);
	}

	@PostMapping("/edit/check/{id}")
	@ApiOperation("提交调查结果")
	public R editCheck(@PathVariable("id") Long id,
					   @Valid @RequestBody ProcessLowCheckDTO checkDTO) {
		BpmProcess process = processService.getByBusId(id);

		ProcessLow processLow = BeanUtil.copyProperties(checkDTO, ProcessLow.class);
		processLow.setId(id);
		processLow.setBpmStatus(ApproveStatusEnum.FINISN.getCode());
		processLow.setCheckReplyTime(new Date());
		Boolean status = lowService.updateById(processLow);
		processService.pass(process.getBpmId());
		if (processService.isProcessEnd(process.getBpmId())) {
			processLow.setBpmStatus(ApproveStatusEnum.FINISN.getCode());
			processLow.setCompleteTime(new Date());
			lowService.updateById(processLow);
		}else {
			processLow.setBpmStatus(ApproveStatusEnum.PROCEED.getCode());
			lowService.updateById(processLow);
		}

		return R.status(status);
	}

	@GetMapping("/self/back")
	@ApiOperation("自撤回接口")
	public R selfBack(@RequestParam("id") Long id) {
		ProcessLow processLow = lowService.getById(id);

		// 非待审批中状态 抛出异常
		if (!ApproveStatusEnum.AWAIT.getCode().equals(processLow.getBpmStatus())) {
			throw new ServiceException("非待审批状态");
		}

		ProcessLow update = new ProcessLow();
		update.setId(id);
		update.setBpmStatus(ApproveStatusEnum.SELF_BACK.getCode());
		update.setBpmNode(-1);

		// 取消当前激活业务
		LambdaUpdateWrapper<BpmProcess> processUpdateWrapper = new LambdaUpdateWrapper<>();
		processUpdateWrapper.eq(BpmProcess::getBusId, id)
			.eq(BpmProcess::getIsCastoff, 0)
			.set(BpmProcess::getBpmStatus, 0);

		// 删除该任务
		processService.update(processUpdateWrapper);
		return R.status(lowService.updateById(update));
	}

	@PostMapping("/save")
	@ApiOperation("新增")
	public R save(@Valid @RequestBody ProcessLowDTO processLowDTO) {
		ProcessLow processLow = BeanUtil.copy(processLowDTO, ProcessLow.class);
		processLow.setCreateUser(CommonUtil.getUserId());
		processLow.setCreateTime(new Date());
		processLow.setCreateDept(CommonUtil.getDeptId());
		processLow.setUpdateUser(CommonUtil.getUserId());
		processLow.setUpdateTime(new Date());
		processLow.setBpmStatus(0);
		processLow.setIsHideApprove(0);
		if (processLow.getType().equals(0)) {
			processLow.setBpmNode(LowBpmNodeEnum.QPR_SAVE.getCode());
		}else {
			processLow.setBpmNode(LowBpmNodeEnum.CHECK_SAVE.getCode());
		}

		processLow.setCode(CodeUtil.getCode(CODE_FLAG));
		processLow.setIsAccessCheck(1);
		return R.status(lowService.saveAndActiveTask(processLow));
	}

	@GetMapping("/delete")
	@ApiOperation("删除")
	public R delete(@RequestParam("ids") String ids) {
		return R.data(lowService.removeByIds(CommonUtil.toLongList(ids)));
	}

	@PostMapping("/update/{id}")
	@ApiOperation("更新")
	public R update(@PathVariable("id") Long id, @Valid  @RequestBody ProcessLowDTO processLowDTO) {
		ProcessLow processLow = BeanUtil.copy(processLowDTO, ProcessLow.class);
		processLow.setId(id);
		processLow.setUpdateUser(CommonUtil.getUserId());
		processLow.setUpdateTime(new Date());
		return R.data(lowService.save(processLow));
	}

	@GetMapping("/page")
	@ApiOperation("分页")
	public R<IPage<ProcessLowVO>> page(ProcessLowVO processLowVO, Query query) {
		LambdaQueryWrapper<ProcessLow> wrapper = new LambdaQueryWrapper<>();
		wrapper.and(StrUtil.isNotBlank(processLowVO.getSearchKey()), item -> {
			item.like(ProcessLow::getTitle, processLowVO.getSearchKey())
				.or()
				.like(ProcessLow::getContent, processLowVO.getSearchKey())
				.or()
				.like(ProcessLow::getDutyDept, processLowVO.getSearchKey());
		});

		if (!new Integer(-1).equals(processLowVO.getBpmStatusFilter())) {
			// 自测回
			if (new Integer(0).equals(processLowVO.getBpmStatusFilter())) {
				wrapper.eq(ProcessLow::getBpmStatus, ApproveStatusEnum.SELF_BACK.getCode());
			}

			// 已驳回
			if (new Integer(1).equals(processLowVO.getBpmStatusFilter())) {
				wrapper.eq(ProcessLow::getBpmStatus, ApproveStatusEnum.BACK.getCode());
			}

			// 进行中
			if (new Integer(2).equals(processLowVO.getBpmStatusFilter())) {
				wrapper.and(item -> {
					item.eq(ProcessLow::getBpmStatus, ApproveStatusEnum.AWAIT.getCode())
						.or()
						.eq(ProcessLow::getBpmStatus, ApproveStatusEnum.PROCEED.getCode());
				});
			}

			// 已办结
			if (new Integer(3).equals(processLowVO.getBpmStatusFilter())) {
				wrapper.eq(ProcessLow::getBpmStatus, ApproveStatusEnum.FINISN.getCode());
			}
		}

		wrapper.eq(processLowVO.getType() != null, ProcessLow::getType, processLowVO.getType())
			.eq(processLowVO.getApparatusType() != null, ProcessLow::getApparatusType, processLowVO.getApparatusType())
			.eq(processLowVO.getTriggerAddress() != null, ProcessLow::getTriggerAddress, processLowVO.getTriggerAddress())
			.in(ProcessLow::getCreateDept, CommonUtil.getDeptIds());
		IPage<ProcessLow> page = lowService.page(Condition.getPage(query), wrapper);
		return R.data(ProcessLowWrapper.build().pageVO(page));
	}

	@GetMapping("/detail")
	@ApiOperation("详情")
	public R<ProcessLowVO> detail(@RequestParam("id") Long id) {
		ProcessLow processLow = lowService.getById(id);
		ProcessLowVO processLowVO = ProcessLowWrapper.build().entityVO(processLow);

		ArrayList<Long> fileIds = new ArrayList<>();
		List<Long> imageReportIds = CommonUtil.toLongList(processLow.getImgReportIds());
		Long separateFileId = processLow.getSeparateFileId();
		Long separateFileDependId = processLow.getSeparateFileDependId();
		List<Long> busincessIds = CommonUtil.toLongList(processLow.getBusincessIdFiles());
		Long testReportFileId = processLow.getTestReportFileId();

		fileIds.addAll(imageReportIds);
		fileIds.addAll(busincessIds);
		fileIds.add(separateFileId);
		fileIds.add(testReportFileId);
		fileIds.add(separateFileDependId);

		LambdaQueryWrapper<BusFile> fileWrapper = new LambdaQueryWrapper<>();
		fileWrapper.in(BusFile::getId, fileIds);
		List<BusFileVO> list = BusFileWrapper.build().listVO(fileService.list(fileWrapper));

		processLowVO.setImgReportList(list.stream().filter(item -> imageReportIds.contains(item.getId())).collect(Collectors.toList()));
		processLowVO.setImgReportList(new ArrayList<>());
		processLowVO.setBusincessFiles(new ArrayList<>());
		for (BusFileVO item : list) {
			if (item.getId().equals(separateFileId)) {
				processLowVO.setSeparateFile(item);
			}
			if (imageReportIds.contains(item.getId())) {
				processLowVO.getImgReportList().add(item);
			}
			if (busincessIds.contains(item.getId())) {
				processLowVO.getBusincessFiles().add(item);
			}
			if (item.getId().equals(testReportFileId)) {
				processLowVO.setTestReportFile(item);
			}

			if (item.getId().equals(separateFileDependId)) {
				processLowVO.setSeparateFileDepend(item);
			}
		}

		return R.data(processLowVO);
	}

	@GetMapping("/quality")
	@ApiOperation("统计接口")
	public R<ProcessLowQualityVO> quality() {
		LambdaQueryWrapper<ProcessLow> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(ProcessLow::getCreateDept, CommonUtil.getDeptIds());
		List<ProcessLow> list = lowService.list(wrapper);

		ProcessLowQualityVO processLowQualityVO = new ProcessLowQualityVO();
		processLowQualityVO.setBack(0);
		processLowQualityVO.setFinish(0);
		processLowQualityVO.setProcess(0);
		processLowQualityVO.setSelfBack(0);

		for (ProcessLow processLow : list) {
			if (processLow.getBpmStatus().equals(ApproveStatusEnum.SELF_BACK.getCode())) {
				processLowQualityVO.setSelfBack(processLowQualityVO.getSelfBack() + 1);
			}
			if (processLow.getBpmStatus().equals(ApproveStatusEnum.BACK.getCode())) {
				processLowQualityVO.setBack(processLowQualityVO.getBack() + 1);
			}
			if (processLow.getBpmStatus().equals(ApproveStatusEnum.AWAIT.getCode()) ||
				processLow.getBpmStatus().equals(ApproveStatusEnum.PROCEED.getCode())) {
				processLowQualityVO.setProcess(processLowQualityVO.getProcess() + 1);
			}
			if (processLow.getBpmStatus().equals(ApproveStatusEnum.FINISN.getCode())) {
				processLowQualityVO.setFinish(processLowQualityVO.getFinish() + 1);
			}
		}
		return R.data(processLowQualityVO);
	}

}
