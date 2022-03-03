/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source codeing must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.common.cache;

import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.system.entity.Role;
import org.springblade.modules.system.service.IRoleService;

/**
 * 系统缓存
 *
 * @author Chill
 */
public class RoleCache {
	private static final String ROLE_CACHE_ID = "role:id:";
	private static final String ROLE_CACHE_ACCOUNT = "role:account:";
	private static final String ROLE_CACHE = "ROLE_CACHE";
	private static final IRoleService roleService;

	static {
		roleService = SpringUtil.getBean(IRoleService.class);
	}

	/**
	 * 获取用户
	 * @return
	 */
	public static Role getRole(Long roleId) {
		return CacheUtil.get(ROLE_CACHE, ROLE_CACHE_ID, roleId, () -> roleService.getById(roleId));
	}

}
