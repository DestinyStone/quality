package org.springblade.modules.di.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.CodeUtil;
import org.springblade.modules.di.bean.entity.DiConfig;
import org.springblade.modules.di.bean.entity.DiReport;
import org.springblade.modules.di.mapper.DiReportMapper;
import org.springblade.modules.di.service.DiReportService;
import org.springblade.modules.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 12:45
 * @Description:
 */
@Service
public class DiReportServiceImpl extends ServiceImpl<DiReportMapper, DiReport> implements DiReportService {

	@Autowired
	private static final String CODE_FLAG = "DI_REPORT_CODE_FLAG";

	@Override
	public Boolean active(DiConfig diConfig, List<User> userList) {
		List<DiReport> collect = userList.stream().map(item -> {
			DiReport common = getCommon(diConfig, item.getId());
			common.setCode(CodeUtil.getCode(CODE_FLAG));
			return common;
		}).collect(Collectors.toList());
		return saveBatch(collect);
	}

	private DiReport getCommon(DiConfig diConfig, Long userId) {
		DiReport diReport = new DiReport();
		diReport.setDiConfigId(diConfig.getId());
		diReport.setDesignation(diConfig.getDesignation());
		diReport.setName(diConfig.getName());
		diReport.setDutyDept(diConfig.getDutyDept());
		diReport.setStatus(0);
		diReport.setCreateTime(new Date());
		diReport.setReportUser(userId);
		if (diConfig.getPastType() == 0) {
			diReport.setPastDay(0);
		}else {
			diReport.setPastDay(diConfig.getPastDay());
		}

		return diReport;
	}
}
