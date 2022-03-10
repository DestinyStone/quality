package org.springblade.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import org.springblade.common.constant.ParamConstant;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.modules.system.entity.Role;
import org.springblade.modules.system.service.IParamService;
import org.springblade.modules.system.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/3 20:12
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/common")
@RestController
@Api(value = "供应商接口", tags = "供应商接口")
public class CommonController {

	@Autowired
	private IParamService paramService;

	@Autowired
	private IRoleService roleService;

	@GetMapping("/provider/list")
	public Map<String, String> getAllProvider() {
		String privateId = paramService.getValue(ParamConstant.PROVIDER_ID);
		if (StrUtil.isBlank(privateId)) {
			return new HashMap<>();
		}

		LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
		roleWrapper.eq(Role::getParentId, privateId)
			.orderByAsc(Role::getSort);
		List<Role> list = roleService.list(roleWrapper);
		return list.stream().collect(Collectors.toMap(item -> item.getId() + "", Role::getRoleName));
	}
}
