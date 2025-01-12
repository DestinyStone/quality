package org.springblade.modules.out_buy_low.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.enums.ApproveStatusEnum;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.file.service.BusFileService;
import org.springblade.modules.out_buy_low.bean.dto.OutBuyQprDTO;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprQualityVO;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprVO;
import org.springblade.modules.out_buy_low.service.OutBuyQprService;
import org.springblade.modules.out_buy_low.utils.OutBuyQprExcelUtil;
import org.springblade.modules.out_buy_low.wrapper.OutBuyQprWrapper;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process_low.enums.LowBpmNodeEnum;
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
 * @Date: 2022/1/28 10:05
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/out/buy/qpr")
@RestController
@Api(value = "工序内不良接口", tags = "工序内不良接口")
public class OutBuyQprController {

	private static final String CODE_FLAG = "LOW";

	@Autowired
	private OutBuyQprService qprService;

	@Autowired
	private BusFileService fileService;

	@Autowired
	private BpmProcessService processService;

	@GetMapping("/account/export")
	@ApiOperation("分页")
	public void export(OutBuyQprVO vo, Query query, HttpServletResponse response) {
		R<IPage<OutBuyQprVO>> page = accountPage(vo, query);
		OutBuyQprExcelUtil.export(page.getData().getRecords(), response);
	}

	@GetMapping("/account/page")
	@ApiOperation("分页")
	public R<IPage<OutBuyQprVO>> accountPage(OutBuyQprVO vo, Query query) {
		R<IPage<OutBuyQprVO>> page = page(vo, query);
		List<OutBuyQprVO> records = page.getData().getRecords();

		if (records.isEmpty()) {
			return page;
		}

		List<Long> fileIds =  records.isEmpty() ? new ArrayList<>() :records.stream().flatMap(item -> {
			return CommonUtil.toLongList(item.getImgReportIds()).stream();
		}).collect(Collectors.toList());
		Map<Long, BusFileVO> fileMap = fileIds.isEmpty() ? new HashMap<>() : fileService.getByIds(fileIds).stream().collect(Collectors.toMap(BusFileVO::getId, Function.identity()));

		for (OutBuyQprVO record : records) {
			if (StrUtil.isNotBlank(record.getImgReportIds())) {
				record.setImgReportFiles(new ArrayList<>());
				for (Long fileId : CommonUtil.toLongList(record.getImgReportIds())) {
					record.getImgReportFiles().add(fileMap.getOrDefault(fileId, new BusFileVO()));
				}
			}
		}
		return page;
	}

	@PostMapping("/re/submit/{id}")
	@ApiOperation("重新提交")
	@Transactional
	public R reSubmit(@PathVariable("id") Long id, @Valid @RequestBody OutBuyQprDTO qprDTO) {
		OutBuyQpr outBuyQpr = BeanUtil.copy(qprDTO, OutBuyQpr.class);
		outBuyQpr.setId(id);
		outBuyQpr.setUpdateUser(CommonUtil.getUserId());
		outBuyQpr.setUpdateTime(new Date());
		outBuyQpr.setBpmStatus(ApproveStatusEnum.AWAIT.getCode());
		outBuyQpr.setBpmNode(LowBpmNodeEnum.QPR_APPROVE.getCode());
		Boolean status = qprService.updateById(outBuyQpr);

		// 废除之前的审批任务
		LambdaUpdateWrapper<BpmProcess> wrapper = new LambdaUpdateWrapper<>();
		wrapper.eq(BpmProcess::getBusId, id)
			.set(BpmProcess::getIsCastoff, 1);
		processService.update(wrapper);

		// 开启审批
		ApproveUtils.createTask(id + "", ApproveUtils.ApproveLinkEnum.OUT_BUY_LOW);


		return R.data(status);
	}

	@GetMapping("/self/back")
	@ApiOperation("自撤回接口")
	public R selfBack(@RequestParam("id") Long id) {
		OutBuyQpr qpr = qprService.getById(id);

		// 非待审批中状态 抛出异常
		if (!ApproveStatusEnum.AWAIT.getCode().equals(qpr.getBpmStatus())) {
			throw new ServiceException("非待审批状态");
		}

		OutBuyQpr update = new OutBuyQpr();
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
		return R.status(qprService.updateById(update));
	}

	@GetMapping("/detail")
	@ApiOperation("详情")
	public R<OutBuyQprVO> detail(@RequestParam("id") Long id) {
		OutBuyQprVO outBuyQprVO = qprService.getDetail(id);
		return R.data(outBuyQprVO);
	}

	@PostMapping("/save")
	@ApiOperation("新增")
	public R save(@Valid @RequestBody OutBuyQprDTO qprDTO) {
		qprDTO.validate();
		OutBuyQpr qpr = BeanUtil.copy(qprDTO, OutBuyQpr.class);
		qpr.setType(0);
		qpr.setIsAccessCheck(1);
		qpr.setBpmNode(LowBpmNodeEnum.QPR_APPROVE.getCode());
		return R.status(qprService.saveAndActiveTask(qpr));
	}

	@GetMapping("/page")
	@ApiOperation("分页")
	public R<IPage<OutBuyQprVO>> page(OutBuyQprVO qprVO, Query query) {
		LambdaQueryWrapper<OutBuyQpr> wrapper = new LambdaQueryWrapper<>();
//		wrapper.eq(OutBuyQpr::getProcessLowFlag, 0)
		if (!CommonUtil.isAdmin()) {
			wrapper.in(OutBuyQpr::getCreateDept, CommonUtil.getDeptIds());
		}
		wrapper.and(StrUtil.isNotBlank(qprVO.getSearchKey()), item -> {
			item.like(OutBuyQpr::getTitle, qprVO.getSearchKey())
				.or()
				.like(OutBuyQpr::getEventRemark, qprVO.getSearchKey())
				.or()
				.like(OutBuyQpr::getDutyDept, qprVO.getSearchKey());
		});

		if (!new Integer(-1).equals(qprVO.getBpmStatusFilter())) {
			// 自测回
			if (new Integer(0).equals(qprVO.getBpmStatusFilter())) {
				wrapper.eq(OutBuyQpr::getBpmStatus, ApproveStatusEnum.SELF_BACK.getCode());
			}

			// 已驳回
			if (new Integer(1).equals(qprVO.getBpmStatusFilter())) {
				wrapper.eq(OutBuyQpr::getBpmStatus, ApproveStatusEnum.BACK.getCode());
			}

			// 进行中
			if (new Integer(2).equals(qprVO.getBpmStatusFilter())) {
				wrapper.and(item -> {
					item.eq(OutBuyQpr::getBpmStatus, ApproveStatusEnum.AWAIT.getCode())
						.or()
						.eq(OutBuyQpr::getBpmStatus, ApproveStatusEnum.PROCEED.getCode());
				});
			}

			// 已办结
			if (new Integer(3).equals(qprVO.getBpmStatusFilter())) {
				wrapper.eq(OutBuyQpr::getBpmStatus, ApproveStatusEnum.FINISN.getCode());
			}
		}

		wrapper.eq(qprVO.getTriggerAddress() != null, OutBuyQpr::getTriggerAddress, qprVO.getTriggerAddress());
		wrapper.orderByDesc(OutBuyQpr::getCreateTime);
		IPage<OutBuyQpr> page = qprService.page(Condition.getPage(query), wrapper);
		return R.data(OutBuyQprWrapper.build().pageVO(page));
	}

	@GetMapping("/quality")
	@ApiOperation("统计接口")
	public R<OutBuyQprQualityVO> quality() {
		LambdaQueryWrapper<OutBuyQpr> wrapper = new LambdaQueryWrapper<>();
		if (!CommonUtil.isAdmin()) {
			wrapper.in(OutBuyQpr::getCreateDept, CommonUtil.getDeptIds());
		}
		wrapper.eq(OutBuyQpr::getProcessLowFlag, 0);
		List<OutBuyQpr> list = qprService.list(wrapper);

		OutBuyQprQualityVO qprQualityVO = new OutBuyQprQualityVO();
		qprQualityVO.setBack(0);
		qprQualityVO.setFinish(0);
		qprQualityVO.setProcess(0);
		qprQualityVO.setSelfBack(0);

		for (OutBuyQpr item : list) {
			if (item.getBpmStatus().equals(ApproveStatusEnum.SELF_BACK.getCode())) {
				qprQualityVO.setSelfBack(qprQualityVO.getSelfBack() + 1);
			}
			if (item.getBpmStatus().equals(ApproveStatusEnum.BACK.getCode())) {
				qprQualityVO.setBack(qprQualityVO.getBack() + 1);
			}
			if (item.getBpmStatus().equals(ApproveStatusEnum.AWAIT.getCode()) ||
				item.getBpmStatus().equals(ApproveStatusEnum.PROCEED.getCode())) {
				qprQualityVO.setProcess(qprQualityVO.getProcess() + 1);
			}
			if (item.getBpmStatus().equals(ApproveStatusEnum.FINISN.getCode())) {
				qprQualityVO.setFinish(qprQualityVO.getFinish() + 1);
			}
		}
		return R.data(qprQualityVO);
	}
}
