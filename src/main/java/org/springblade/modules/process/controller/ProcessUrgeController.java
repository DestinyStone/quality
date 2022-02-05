package org.springblade.modules.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.process.entity.bean.BpmProcessUrge;
import org.springblade.modules.process.entity.dto.BpmProcessUrgeDTO;
import org.springblade.modules.process.entity.vo.BpmProcessUrgeVO;
import org.springblade.modules.process.service.ProcessUrgeService;
import org.springblade.modules.process.wrapper.BpmProcessUrgeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 11:56
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/process/urge")
@RestController
@Api(value = "工序催办", tags = "工序催办")
public class ProcessUrgeController {

	@Autowired
	private ProcessUrgeService urgeService;

	@PostMapping("/save")
	@ApiOperation("保存")
	public R save(@Valid @RequestBody BpmProcessUrgeDTO urgeDTO) {
		BpmProcessUrge processUrge = BeanUtil.copyProperties(urgeDTO, BpmProcessUrge.class);
		processUrge.setCreateTime(new Date());
		processUrge.setCreateDept(CommonUtil.getDeptId());
		processUrge.setCreateDeptName(CommonUtil.getDeptPath());
		processUrge.setCreateUserName(CommonUtil.getUserName());
		processUrge.setCreateUser(CommonUtil.getUserId());
		processUrge.setCreateRole(CommonUtil.getRoleName());
		return R.status(urgeService.save(processUrge));
	}


	@GetMapping("/page")
	@ApiOperation("分页")
	public R<IPage<BpmProcessUrgeVO>> page(BpmProcessUrgeVO processUrgeVO, Query query) {
		LambdaQueryWrapper<BpmProcessUrge> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcessUrge::getBpmId, processUrgeVO.getBpmId());
		return R.data(BpmProcessUrgeWrapper.build().pageVO(urgeService.page(Condition.getPage(query), wrapper)));
	}

	@GetMapping("/list/{bpmId}")
	@ApiOperation("列表")
	public R<List<BpmProcessUrgeVO>> list(@PathVariable("bpmId") Long bpmId,  BpmProcessUrgeVO processUrgeVO) {
		LambdaQueryWrapper<BpmProcessUrge> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcessUrge::getBpmId, bpmId);
		wrapper.orderByDesc(BpmProcessUrge::getCreateTime);
		return R.data(BpmProcessUrgeWrapper.build().listVO(urgeService.list(wrapper)));
	}
}
