package org.springblade.modules.process_low.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
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
import org.springblade.modules.process_low.bean.dto.ProcessLowDTO;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.bean.vo.ProcessLowVO;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springblade.modules.process_low.wrapper.ProcessLowWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 11:26
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/process/low")
@RestController
@Api(value = "工序内不良接口", tags = "工序内不良接口")
public class ProcessLowController {

	@Autowired
	private ProcessLowService lowService;

	@GetMapping("/self/back")
	@ApiOperation("自撤回接口")
	public R selfBack(@RequestParam("id") Long id) {
		ProcessLow processLow = lowService.getById(id);

		// 非待审批中状态 抛出异常
		if (!ApproveStatusEmun.AWAIT.getCode().equals(processLow.getBpmStatus())) {
			throw new ServiceException("非待审批状态");
		}

		ProcessLow update = new ProcessLow();
		update.setId(id);
		update.setBpmStatus(ApproveStatusEmun.SELF_BACK.getCode());
		return R.status(lowService.updateById(update));
	}

	@PostMapping("/save")
	@ApiOperation("详情")
	public R save(@Valid @RequestBody ProcessLowDTO processLowDTO) {
		ProcessLow processLow = BeanUtil.copy(processLowDTO, ProcessLow.class);
		processLow.setCreateUser(CommonUtil.getUserId());
		processLow.setCreateTime(new Date());
		processLow.setCreateDept(CommonUtil.getDeptId());
		processLow.setUpdateUser(CommonUtil.getUserId());
		processLow.setUpdateTime(new Date());
		processLow.setBpmStatus(0);
		return R.status(lowService.save(processLow));
	}

	@GetMapping("/delete")
	@ApiOperation("详情")
	public R delete(@RequestParam("ids") String ids) {
		return R.data(lowService.removeByIds(CommonUtil.toLongList(ids)));
	}

	@PostMapping("/update/{id}")
	@ApiOperation("详情")
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
				.like(ProcessLow::getContent, processLowVO.getContent())
				.or()
				.like(ProcessLow::getDutyDept, processLowVO.getDutyDept());
		});

		if (!new Integer(-1).equals(processLowVO.getBpmStatusFilter())) {
			// 自测回
			if (new Integer(0).equals(processLowVO.getBpmStatusFilter())) {
				wrapper.eq(ProcessLow::getBpmStatus, ApproveStatusEmun.SELF_BACK.getCode());
			}

			// 已驳回
			if (new Integer(1).equals(processLowVO.getBpmStatusFilter())) {
				wrapper.eq(ProcessLow::getBpmStatus, ApproveStatusEmun.BACK.getCode());
			}

			// 进行中
			if (new Integer(2).equals(processLowVO.getBpmStatusFilter())) {
				wrapper.and(item -> {
					item.eq(ProcessLow::getBpmStatus, ApproveStatusEmun.AWAIT.getCode())
						.or()
						.eq(ProcessLow::getBpmStatus, ApproveStatusEmun.PROCEED.getCode());
				});
			}

			// 已办结
			if (new Integer(3).equals(processLowVO.getBpmStatusFilter())) {
				wrapper.eq(ProcessLow::getBpmStatus, ApproveStatusEmun.FINISN.getCode());
			}
		}

		wrapper.eq(processLowVO.getType() != null, ProcessLow::getType, processLowVO.getType())
			.eq(processLowVO.getApparatusType() != null, ProcessLow::getApparatusType, processLowVO.getApparatusType())
			.eq(processLowVO.getTriggerAddress() != null, ProcessLow::getTriggerAddress, processLowVO.getTriggerAddress());

		IPage<ProcessLow> page = lowService.page(Condition.getPage(query), wrapper);
		return R.data(ProcessLowWrapper.build().pageVO(page));
	}

	@GetMapping("/detail")
	@ApiOperation("详情")
	public R<ProcessLowVO> detail(@RequestParam("id") Long id) {
		ProcessLow processLow = lowService.getById(id);
		return R.data(ProcessLowWrapper.build().entityVO(processLow));
	}
}
