package org.springblade.modules.check.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qcloud.cos.event.DeliveryMode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.check.bean.dto.CheckDTO;
import org.springblade.modules.check.bean.dto.CheckResourceDTO;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.AccessSaveCheckVO;
import org.springblade.modules.check.service.CheckService;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprApproveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
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

	@Autowired
	private CheckService checkService;

	@PostMapping("/save")
	public R save(@Valid @RequestBody CheckDTO checkDTO) {
		 List<CheckResourceDTO> resourceList = checkDTO.getResourceList();
		List<Check> collect = resourceList.stream().map(item -> {
			Check copy = CommonUtil.copy(checkDTO, Check.class);
			copy.setResourceId(item.getResourceId());
			copy.setResourceType(item.getResourceType());
			copy.setBpmNode(0);
			copy.setCreateUser(CommonUtil.getUserId());
			copy.setCreateDept(CommonUtil.getDeptId());
			copy.setCreateTime(new Date());
			copy.setUpdateUser(CommonUtil.getUserId());
			copy.setUpdateTime(new Date());
			return copy;
		}).collect(Collectors.toList());

		return R.status(checkService.saveBatch(collect));
	}

	@GetMapping("/access/save/page")
	@ApiOperation("分页")
	public R<IPage<AccessSaveCheckVO>> page(AccessSaveCheckVO approveVO, Query query) {
		approveVO.setDeptId(CommonUtil.getDeptId());
		IPage<AccessSaveCheckVO> page = checkService.accessSavePage(approveVO, Condition.getPage(query));
		return R.data(page);
	}

}

