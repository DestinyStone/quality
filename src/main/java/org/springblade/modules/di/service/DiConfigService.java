package org.springblade.modules.di.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.di.bean.entity.DiConfig;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/3 18:23
 * @Description:
 */
public interface DiConfigService extends IService<DiConfig> {
	/**
	 * 提交
	 * @param collect
	 * @return
	 */
	Boolean submit(List<DiConfig> collect);

	/**
	 * 触发
	 * @param diConfig
	 * @return
	 */
	Boolean trigger(DiConfig diConfig);

	DiConfig getOne(Long resourceId, Integer resourceType);
}
