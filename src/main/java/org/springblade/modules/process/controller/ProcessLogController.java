package org.springblade.modules.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.core.tool.api.R;
import org.springblade.modules.process.entity.bean.BpmProcessLog;
import org.springblade.modules.process.entity.vo.BpmProcessLogVO;
import org.springblade.modules.process.entity.vo.BpmProcessUrgeVO;
import org.springblade.modules.process.service.BpmProcessLogService;
import org.springblade.modules.process.wrapper.BpmProcessLogWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 14:30
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/process/log")
@RestController
@Api(value = "流程日志", tags = "流程日志")
public class ProcessLogController {

	@Autowired
	private BpmProcessLogService logService;

	@GetMapping("/list/{bpmId}")
	@ApiOperation("列表")
	public R<List<BpmProcessLogVO>> list(@PathVariable("bpmId") Long bpmId, BpmProcessUrgeVO processUrgeVO) {
		LambdaQueryWrapper<BpmProcessLog> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcessLog::getBpmId, bpmId);
		wrapper.orderByAsc(BpmProcessLog::getOperatorTime);
		return R.data(BpmProcessLogWrapper.build().listVO(logService.list(wrapper)));
	}

}
