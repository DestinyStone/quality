package org.springblade.modules.di.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.enums.ApproveStatusEnum;
import org.springblade.common.utils.CodeUtil;
import org.springblade.modules.di.bean.dto.DiReportSubmitDTO;
import org.springblade.modules.di.bean.entity.DiConfig;
import org.springblade.modules.di.bean.entity.DiReport;
import org.springblade.modules.di.mapper.DiReportMapper;
import org.springblade.modules.di.service.DiConfigService;
import org.springblade.modules.di.service.DiReportService;
import org.springblade.modules.di.utils.DiEmailUtils;
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

	@Autowired
	private DiConfigService configService;

	@Override
	public Boolean active(DiConfig diConfig, List<User> userList) {
		List<DiReport> collect = userList.stream().map(item -> {
			DiReport common = getCommon(diConfig, item.getId());
			common.setCode(CodeUtil.getCode(CODE_FLAG));
			return common;
		}).collect(Collectors.toList());
		Boolean status = saveBatch(collect);
		for (DiReport diReport : collect) {
			DiEmailUtils.sendEmail(diReport);
		}
		return status;
	}

	@Override
	public Boolean report(Long id, DiReportSubmitDTO submitDTO) {
		DiReport diReport = new DiReport();
		diReport.setId(id);
		diReport.setStatus(1);
		diReport.setBusinessType(0);
		diReport.setBpmStatus(ApproveStatusEnum.AWAIT.getCode());
		diReport.setReportTime(new Date());
		diReport.setDataExcelFileId(submitDTO.getDataExcelFileId());
		diReport.setDataExcelFileName(submitDTO.getDataExcelFileName());
		diReport.setDataSignateFileId(submitDTO.getDataSignateFileId());
		diReport.setDataSignateFileName(submitDTO.getDataSignateFileName());

		Boolean status = updateById(diReport);
		return status;
	}

	@Override
	public void updateConfig(Long id) {
		DiReport current = getById(id);
		if (current.getBusinessType() == 0) {
			configService.updateNewVersion(current.getDiConfigId(), current.getDataExcelFileId(), current.getDataExcelFileName());
		}
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
