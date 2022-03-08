package org.springblade.modules.di.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.common.cache.DiConfigCache;
import org.springblade.modules.di.bean.entity.DiConfig;
import org.springblade.modules.di.mapper.DiConfigMapper;
import org.springblade.modules.di.service.DiConfigService;
import org.springblade.modules.di.service.DiReportService;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
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

	private Logger logger = LoggerFactory.getLogger(DiConfigServiceImpl.class);

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
				DiConfigCache.evict(one.getId());
			}
			// 如果存在立即触发
			if (diConfig.getCycleType().contains("2") && diConfig.getStatus() == 1) {
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

	@Override
	public void updateNewVersion(Long diConfigId, Long dataExcelFileId, String dataExcelFileName) {
		DiConfig diConfig = new DiConfig();
		diConfig.setId(diConfigId);
		diConfig.setLastFileId(dataExcelFileId);
		diConfig.setLastFileName(dataExcelFileName);
		updateById(diConfig);
		DiConfigCache.evict(diConfigId);
	}

	@Override
	public Boolean updateEnableStatus(Long id, Integer status) {
		DiConfig diConfig = getById(id);
		diConfig.setId(id);
		diConfig.setStatus(status);
		diConfig.setUpdateTime(new Date());
		Boolean updateStatus = updateById(diConfig);
		if (status == 1) {
			trigger(diConfig);
		}

		DiConfigCache.evict(id);
		return updateStatus;
	}

	@Override
	public void triggerAll() {
		LambdaQueryWrapper<DiConfig> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(DiConfig::getStatus, 1);
		List<DiConfig> list = list(wrapper);
		for (DiConfig diConfig : list) {
			// 选择特定时间触发
			if (diConfig.getCycleType().contains("1") && equalsDate(diConfig.getCycleTime(), new Date())) {
				trigger(diConfig);
			}
			// 每月一号触发
			if (diConfig.getCycleType().contains("0") && isMonthLastOne(new Date())) {
				// 用户选定了特定日期， 恰好是今天, 则不触发
				if (diConfig.getCycleType().contains("1") && equalsDate(diConfig.getCycleTime(), new Date())) {
					continue;
				}
				// 进行触发
				trigger(diConfig);
			}
		}
	}

	private boolean isMonthLastOne(Date target) {
		Calendar targetCalendar = Calendar.getInstance();
		targetCalendar.setTime(target);
		return targetCalendar.get(Calendar.DAY_OF_MONTH) == 1;
	}

	private boolean equalsDate(Date target, Date current) {
		Calendar targetCalendar = Calendar.getInstance();
		targetCalendar.setTime(target);

		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(current);

		if (targetCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
			targetCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
			targetCalendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)){
			return true;
		}
		return false;
	}
}
