package org.springblade.modules.di.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.di.bean.entity.DiConfig;
import org.springblade.modules.di.bean.entity.DiReport;
import org.springblade.modules.system.entity.User;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 12:45
 * @Description:
 */
public interface DiReportService extends IService<DiReport> {
	/**
	 * 激活任务
	 * @param diConfig
	 * @param userList
	 * @return
	 */
	Boolean active(DiConfig diConfig, List<User> userList);
}
