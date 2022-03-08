package org.springblade.common.time;

import org.springblade.modules.di.service.DiConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/7 14:04
 * @Description:
 */
@Component
public class DiReportScheduler {

	private static DiConfigService diConfigService;

	// 每天凌晨执行
	@Scheduled(cron = "0 0 0 ? * *")
	public void task() {
		diConfigService.triggerAll();
	}

	@Autowired
	public void setDiConfigService(DiConfigService diConfigService) {
		DiReportScheduler.diConfigService = diConfigService;
	}
}
