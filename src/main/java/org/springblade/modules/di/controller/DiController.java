package org.springblade.modules.di.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.cache.RoleCache;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.di.bean.dto.DiConfigSubmitDTO;
import org.springblade.modules.di.bean.entity.DiConfig;
import org.springblade.modules.di.bean.vo.DiAccountVO;
import org.springblade.modules.di.service.DiConfigService;
import org.springblade.modules.di.service.DiService;
import org.springblade.modules.di.wrapper.DiConfigWrapper;
import org.springblade.modules.out_buy_low.bean.dto.ResourceDTO;
import org.springblade.modules.out_buy_low.utils.ResourceConvertUtil;
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

	@GetMapping("/account/page")
	@ApiOperation("分页")
	public R<IPage<DiAccountVO>> accountPage(DiAccountVO vo, Query query) {
		IPage<DiAccountVO> page = diService.accountPage(vo, Condition.getPage(query));
		for (DiAccountVO record : page.getRecords()) {
			Role role = RoleCache.getRole(Long.parseLong(record.getDutyDept()));
			if (role != null) {
				record.setDutyDept(role.getRoleName());
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

}
