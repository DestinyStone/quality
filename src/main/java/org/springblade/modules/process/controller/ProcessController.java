package org.springblade.modules.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.api.R;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.dto.PutOfDTO;
import org.springblade.modules.process.entity.vo.BpmProcessVO;
import org.springblade.modules.process.service.BpmProcessService;
import org.springblade.modules.process.wrapper.BpmProcessWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/29 00:28
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/process")
@RestController
@Api(value = "工序内不良接口", tags = "工序内不良接口")
public class ProcessController {

	@Autowired
	private BpmProcessService processService;

	@GetMapping("/list")
	@ApiOperation("列表")
	public R<List<BpmProcessVO>> list(@RequestParam("busId") String busId) {
		LambdaQueryWrapper<BpmProcess> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcess::getBusId, busId)
			.eq(BpmProcess::getIsCastoff, 0);
		List<BpmProcess> list = processService.list(wrapper);
		return R.data(BpmProcessWrapper.build().listVO(list));
	}

	@PostMapping("/put/of")
	@ApiOperation("延迟接口")
	public R page(@RequestBody @Valid PutOfDTO putOfDTO) {
		BpmProcess process = processService.getById(putOfDTO.getBpmId());
		if (process == null) {
			throw new ServiceException("流程不存在");
		}

		if (!new Integer(1).equals(process.getBpmPushStatus())) {
			throw new ServiceException("任务未超期");
		}

		if (putOfDTO.getEndTime().getTime() < System.currentTimeMillis()) {
			throw new ServiceException("延迟时间必须大于当前时间");
		}

		LambdaUpdateWrapper<BpmProcess> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(BpmProcess::getBpmId, putOfDTO.getBpmId())
			.set(BpmProcess::getEndTime, putOfDTO.getEndTime())
			.set(BpmProcess::getPutOfRemark, putOfDTO.getRemark())
			.set(BpmProcess::getBpmPushStatus, 2);
		return R.data(processService.update(updateWrapper));
	}
}
