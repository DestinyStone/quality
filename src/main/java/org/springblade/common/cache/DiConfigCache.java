package org.springblade.common.cache;

import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springblade.modules.di.bean.entity.DiConfig;
import org.springblade.modules.di.service.DiConfigService;

import static org.springblade.core.cache.constant.CacheConstant.USER_CACHE;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/7 11:37
 * @Description:
 */
public class DiConfigCache {
	private static final String DI_CONFIG_CACHE_ID = "di:config:id";
	private static final String DI_CONFIG_ACCOUNT = "di:config:account";

	private static final DiConfigService configService;

	static {
		configService = SpringUtil.getBean(DiConfigService.class);
	}

	/**
	 * 获取DI配置
	 * @return
	 */
	public static DiConfig getById(Long resourceId, Integer resourceType) {
		DiConfig one = configService.getOne(resourceId, resourceType);
		if (one == null) {
			return null;
		}
		return CacheUtil.get(USER_CACHE, DI_CONFIG_CACHE_ID, one.getId(), () -> {
			return one;
		});
	}

	/**
	 * 获取DI配置
	 * @return
	 */
	public static DiConfig getById(Long id) {
		return CacheUtil.get(USER_CACHE, DI_CONFIG_CACHE_ID, id, () -> configService.getById(id));
	}

	/**
	 * 移除缓存
	 *
	 * @param id 标准分类ID
	 */
	public static void evict(Long id) {
		CacheUtil.evict(USER_CACHE, DI_CONFIG_CACHE_ID, id);
	}

	private static String getPrefix() {
		return DI_CONFIG_CACHE_ID.concat(StringPool.DASH).concat(AuthUtil.getTenantId()).concat(StringPool.COLON);
	}
}
