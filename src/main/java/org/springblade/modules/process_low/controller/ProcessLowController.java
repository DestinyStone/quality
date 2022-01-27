package org.springblade.modules.process_low.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.asm.Advice;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.utils.CommonUtil;
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

	@PostMapping("/save")
	@ApiOperation("详情")
	public R save(@Valid @RequestBody ProcessLowDTO processLowDTO) {
		ProcessLow processLow = BeanUtil.copy(processLowDTO, ProcessLow.class);
		processLow.setCreateUser(CommonUtil.getUserId());
		processLow.setCreateTime(new Date());
		processLow.setCreateDept(CommonUtil.getDeptId());
		processLow.setUpdateUser(CommonUtil.getUserId());
		processLow.setUpdateTime(new Date());
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
