package org.springblade.modules.di.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.di.bean.entity.DiConfig;
import org.springblade.modules.di.mapper.DiConfigMapper;
import org.springblade.modules.di.service.DiConfigService;
import org.springblade.modules.di.service.DiReportService;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/3 18:23
 * @Description:
 */
@Service
public class DiConfigServiceImpl extends ServiceImpl<DiConfigMapper, DiConfig> implements DiConfigService {

	@Autowired
	private IUserService userService;

	@Autowired
	private DiReportService reportService;

	@Override
	public synchronized Boolean submit(List<DiConfig> collect) {
		for (DiConfig diConfig : collect) {
			// 查询该配置项是否存在
			DiConfig one = getOne(diConfig.getResourceId(), diConfig.getResourceType());
			if (one == null) {
				save(diConfig);
			}else {
				diConfig.setId(one.getId());
				updateById(diConfig);
			}
			// 如果存在立即触发
			if (diConfig.getCycleType().contains("2")) {
				trigger(diConfig);
			}

		}
		return true;
	}

	@Override
	public Boolean trigger(DiConfig diConfig) {

		// 找到所有供应商用户
		List<User> userList = userService.getByProviderId(diConfig.getDutyDept());
		if (userList.isEmpty()) {
			return false;
		}
		return reportService.active(diConfig, userList);
	}


	@Override
	public DiConfig getOne(Long resourceId, Integer resourceType) {
		LambdaQueryWrapper<DiConfig> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DiConfig::getResourceId, resourceId)
			.eq(DiConfig::getResourceType, resourceType);
		return getOne(wrapper);
	}
}
