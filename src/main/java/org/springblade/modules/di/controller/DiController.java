package org.springblade.modules.di.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.cache.DiConfigCache;
import org.springblade.common.cache.RoleCache;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.enums.ApproveStatusEnum;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.di.bean.dto.DiConfigSubmitDTO;
import org.springblade.modules.di.bean.dto.DiReportSubmitDTO;
import org.springblade.modules.di.bean.entity.DiConfig;
import org.springblade.modules.di.bean.entity.DiReport;
import org.springblade.modules.di.bean.vo.DiAccountVO;
import org.springblade.modules.di.bean.vo.DiReportQuality;
import org.springblade.modules.di.bean.vo.DiReportVO;
import org.springblade.modules.di.service.DiConfigService;
import org.springblade.modules.di.service.DiReportApproveService;
import org.springblade.modules.di.service.DiReportService;
import org.springblade.modules.di.service.DiService;
import org.springblade.modules.di.wrapper.DiConfigWrapper;
import org.springblade.modules.di.wrapper.DiReportWrapper;
import org.springblade.modules.out_buy_low.bean.dto.ResourceDTO;
import org.springblade.modules.out_buy_low.utils.ResourceConvertUtil;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.system.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/25 11:27
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/di")
@RestController
@Api(value = "Di接口", tags = "Di接口")
public class DiController {

	@Autowired
	private DiService diService;

	@Autowired
	private DiConfigService diConfigService;

	@Autowired
	private DiReportService reportService;

	@Autowired
	private DiReportApproveService diApproveService;

	@Autowired
	private BpmProcessService processService;

	@GetMapping("/version/page")
	@ApiOperation("Di数据版本")
	public R<IPage<DiReportVO>> versionPage(@RequestParam("resourceId") Long resourceId,
										  @RequestParam("resourceType") Integer resourceType,
										  Query query) {
		// 确定di配置
		DiConfig diConfig = diConfigService.getOne(resourceId, resourceType);
		if (diConfig == null) {
			return R.data(Condition.getPage(query));
		}
		LambdaQueryWrapper<DiReport> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DiReport::getBpmStatus, ApproveStatusEnum.FINISN.getCode())
			.eq(DiReport::getDiConfigId, diConfig.getId())
			.orderByDesc(DiReport::getReportTime);
		return R.data(DiReportWrapper.build().pageVO(reportService.page(Condition.getPage(query), wrapper)));
	}

	@GetMapping("/account/page")
	@ApiOperation("分页")
	public R<IPage<DiAccountVO>> accountPage(DiAccountVO vo, Query query) {
		IPage<DiAccountVO> page = diService.accountPage(vo, Condition.getPage(query));
		for (DiAccountVO record : page.getRecords()) {
			Role role = RoleCache.getRole(Long.parseLong(record.getDutyDept()));
			if (role != null) {
				record.setDutyDept(role.getRoleName());
			}
			DiConfig diConfig = DiConfigCache.getById(record.getResourceId(), record.getResourceType());
			if (diConfig != null) {
				record.setStatus(diConfig.getStatus());
				record.setLastFileId(diConfig.getLastFileId());
				record.setLastFileName(diConfig.getLastFileName());
				record.setUpdateTime(diConfig.getUpdateTime());
			}else {
				record.setStatus(0);
			}

		}
		return R.data(page);
	}

	@PostMapping("/submit")
	@ApiOperation("提交配置")
	public R submit(@Valid @RequestBody DiConfigSubmitDTO submitDTO) {
		submitDTO.validate();

		List<ResourceDTO.Encumbrance> resourceList = ResourceConvertUtil.convert(submitDTO.getResourceList());
		Map<Long, ResourceDTO.Encumbrance> encumbranceMap = ResourceConvertUtil.convertMap(resourceList);

		List<DiConfig> collect = resourceList.stream().map(item -> {
			DiConfig copy = CommonUtil.copy(submitDTO, DiConfig.class);
			copy.setResourceId(item.getResourceId());

			ResourceDTO.Encumbrance encumbrance = encumbranceMap.getOrDefault(item.getResourceId(),  new ResourceDTO.Encumbrance());
			copy.setName(encumbrance.getName());
			copy.setDesignation(encumbrance.getDesignation());
			copy.setDutyDept(encumbrance.getDutyDept());
			copy.setResourceType(encumbrance.getResourceType());
			copy.setUpdateDept(CommonUtil.getDeptId());
			copy.setUpdateUser(CommonUtil.getUserId());
			copy.setUpdateTime(new Date());
			if (copy.getStatus() == null) {
				copy.setStatus(1);
			}
			return copy;
		}).collect(Collectors.toList());

		return R.data(diConfigService.submit(collect));
	}

	@GetMapping("/detail")
	@ApiOperation("查看详情")
	public R detail(@RequestParam("resourceId") Long resourceId, @RequestParam("resourceType") Integer resourceType) {
		DiConfig diConfig = diConfigService.getOne(resourceId, resourceType);
		return R.data(DiConfigWrapper.build().entityVO(diConfig));
	}

	@GetMapping("/report/page")
	@ApiOperation("查看详情")
	public R<IPage<DiReportVO>> reportPage(DiReportVO reportVO, Query query) {
		LambdaQueryWrapper<DiReport> wrapper = getCommonWrapper(reportVO);
		wrapper.eq(DiReport::getStatus, 1)
			.eq(DiReport::getReportUser, CommonUtil.getUserId());
		if (reportVO.getTag() == null) {
			reportVO.setTag(0);
		}
		if (reportVO.getTag() == 1) {
			wrapper.eq(DiReport::getBpmStatus, ApproveStatusEnum.AWAIT.getCode());
		}
		if (reportVO.getTag() == 2) {
			wrapper.eq(DiReport::getBpmStatus, ApproveStatusEnum.BACK.getCode());
		}
		if (reportVO.getTag() == 3) {
			wrapper.eq(DiReport::getBpmStatus, ApproveStatusEnum.FINISN.getCode());
		}
		return R.data(DiReportWrapper.build().pageVO(reportService.page(Condition.getPage(query), wrapper)));
	}

	@GetMapping("/enable")
	@ApiOperation("启用禁用")
	public R enable(@RequestParam("resourceId") Long resourceId,
					@RequestParam("resourceType") Integer resourceType,
					@RequestParam("status") Integer status) {
		DiConfig config = diConfigService.getOne(resourceId, resourceType);
		if (config == null) {
			throw new ServiceException("请先配置上报周期");
		}
		return R.status(diConfigService.updateEnableStatus(config.getId(), status));
	}

	@GetMapping("/await/report/page")
	@ApiOperation("查看详情")
	public  R<IPage<DiReportVO>> awaitReportPage(DiReportVO reportVO, Query query) {
		LambdaQueryWrapper<DiReport> wrapper = getCommonWrapper(reportVO);
		wrapper.eq(DiReport::getStatus, 0)
			.eq(DiReport::getReportUser, CommonUtil.getUserId());
		return R.data(DiReportWrapper.build().pageVO(reportService.page(Condition.getPage(query), wrapper)));
	}

	@PostMapping("/re/report/{id}")
	@ApiOperation("重新上报")
	public R reReport(@PathVariable("id") Long id, @RequestBody @Valid DiReportSubmitDTO submitDTO) {
		DiReport report = reportService.getById(id);
		if (new Integer(1).equals(report.getStatus()) && !ApproveStatusEnum.BACK.getCode().equals(report.getBpmStatus())) {
			throw new ServiceException("已上报, 不可重复上报");
		}
		reportService.report(id, submitDTO);

		// 废除之前的审批任务
		LambdaUpdateWrapper<BpmProcess> wrapper = new LambdaUpdateWrapper<>();
		wrapper.eq(BpmProcess::getBusId, id)
			.set(BpmProcess::getIsCastoff, 1);
		processService.update(wrapper);

		diApproveService.create(id);
		return R.status(true);
	}

	@PostMapping("/report/{id}")
	@ApiOperation("上报")
	public R report(@PathVariable("id") Long id, @RequestBody @Valid DiReportSubmitDTO submitDTO) {
		DiReport report = reportService.getById(id);

		if (new Integer(1).equals(report.getStatus())) {
			throw new ServiceException("已上报, 不可重复上报");
		}

		reportService.report(id, submitDTO);
		diApproveService.create(id);
		return R.status(true);
	}

	@GetMapping("/report/quality")
	@ApiOperation("上报统计")
	public R<DiReportQuality> reportQuality() {
		LambdaQueryWrapper<DiReport> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DiReport::getStatus, 1)
			.eq(DiReport::getReportUser, CommonUtil.getUserId())
			.select(DiReport::getBpmStatus);
		List<DiReport> list = reportService.list(wrapper);

		DiReportQuality init = DiReportQuality.getInit();
		for (DiReport report : list) {
			if (ApproveStatusEnum.AWAIT.getCode().equals(report.getBpmStatus())) {
				init.setAwait(init.getAwait() + 1);
			}

			if (ApproveStatusEnum.BACK.getCode().equals(report.getBpmStatus())) {
				init.setBack(init.getBack() + 1);
			}

			if (ApproveStatusEnum.FINISN.getCode().equals(report.getBpmStatus())) {
				init.setFinish(init.getFinish() + 1);
			}
		}
		return R.data(init);
	}

	@GetMapping("/self/back")
	@ApiOperation("自撤回")
	public  R selfBack(@RequestParam("id") Long id) {
		DiReport report = reportService.getById(id);
		if (!report.getBpmStatus().equals(ApproveStatusEnum.AWAIT.getCode())) {
			throw new ServiceException("审批已进行 不可退回");
		}
		LambdaUpdateWrapper<DiReport> update = new LambdaUpdateWrapper<>();
		update.eq(DiReport::getId, id)
			.set(DiReport::getStatus, 0)
			.set(DiReport::getBusinessType, null)
			.set(DiReport::getReportTime, null)
			.set(DiReport::getNoReportRemark, null)
			.set(DiReport::getDataExcelFileId, null)
			.set(DiReport::getDataExcelFileName, null)
			.set(DiReport::getDataSignateFileId, null)
			.set(DiReport::getDataSignateFileName, null);
		reportService.update(update);

		// 取消当前激活业务
		LambdaUpdateWrapper<BpmProcess> processUpdateWrapper = new LambdaUpdateWrapper<>();
		processUpdateWrapper.eq(BpmProcess::getBusId, id)
			.eq(BpmProcess::getIsCastoff, 0)
			.set(BpmProcess::getBpmStatus, 0);

		// 删除该任务
		processService.update(processUpdateWrapper);
		return R.status(true);
	}

	@GetMapping("/report/detail")
	@ApiOperation("上报详情")
	public  R<DiReportVO> reportDetail(@RequestParam("id") Long id) {
		DiReport report = reportService.getById(id);
		return R.data(DiReportWrapper.build().entityVO(report));
	}

	@GetMapping("/un/report/{id}")
	@ApiOperation("无需上报")
	public  R unReportId(@PathVariable("id") Long id, @RequestParam("remark") String remark) {
		if (StrUtil.isBlank(remark)) {
			throw new ServiceException("无需上报理由为空");
		}
		DiReport diReport = new DiReport();
		diReport.setId(id);
		diReport.setStatus(1);
		diReport.setBusinessType(2);
		diReport.setBpmStatus(ApproveStatusEnum.AWAIT.getCode());
		diReport.setNoReportRemark(remark);
		diReport.setReportTime(new Date());

		boolean status = reportService.updateById(diReport);
		if (status) {
			ApproveUtils.createTask(id + "", ApproveUtils.ApproveLinkEnum.DI_UN_REPORT);
		}
		return R.status(status);
	}

	private LambdaQueryWrapper<DiReport> getCommonWrapper(DiReportVO reportVO) {
		LambdaQueryWrapper<DiReport> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DiReport::getReportUser, CommonUtil.getUserId())
			.and(StrUtil.isNotBlank(reportVO.getSearchKey()), item -> {
				item.like(DiReport::getName, reportVO.getSearchKey()).or()
					.like(DiReport::getDesignation, reportVO.getSearchKey()).or()
					.like(DiReport::getDutyDept, reportVO.getDutyDept());
			});
		wrapper.orderByDesc(DiReport::getReportTime);
		return wrapper;
	}
}
