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
package org.springblade.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springblade.common.cache.SysCache;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.system.entity.Dept;
import org.springframework.lang.Nullable;
import org.springframework.util.PropertyPlaceholderHelper;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 11:26
 * @Description: 通用工具类， 系统工具的统一维护
 */
public class CommonUtil {

	private static final Snowflake snowflake = IdUtil.createSnowflake(1, 1);
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}",":", false);

	public static String placeHolderReplace(String value, Map<String, String> map) {
		if (map == null || map.keySet().isEmpty()) {
			return value;
		}
		Properties properties = new Properties();
		Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> next = iterator.next();
			properties.setProperty(next.getKey(), next.getValue());
		}

		return helper.replacePlaceholders(value, properties);
	}

	public static String dateParseString(Date date) {
		if (date == null) {
			return "";
		}
		return format.format(date);
	}

	/**
	 * 获取唯一id
	 */
	public static Long getCode() {
		return snowflake.nextId();
	}

	/**
	 * 获取当前用户ID
	 */
	public static Long getUserId() {
		BladeUser user = AuthUtil.getUser();
		return user.getUserId();
	}

	/**
	 * 获取当前用户名称
	 */
	public static String getUserName() {
		BladeUser user = AuthUtil.getUser();
		return user.getUserName();
	}

	/**
	 * 获取用户部门Ids
	 */
	public static List<Long> getDeptIds() {
		String deptId = AuthUtil.getDeptId();
		return toLongList(deptId);
	}

	/**
	 * 获取用户部门
	 */
	public static Long getDeptId() {
		String deptId = AuthUtil.getDeptId();
		return firstLong(deptId);
	}

	public static Long getRoleId() {
		List<Long> roleIds = toLongList(AuthUtil.getUser().getRoleId());
		return roleIds.isEmpty() ? null : roleIds.get(0);
	}

	/**
	 * 获取部门路径
	 */
	public static String getDeptPath() {
		Long deptId = getDeptId();
		Dept dept = SysCache.getDept(deptId);
		return dept == null ? "" : dept.getDeptName();
	}

	/**
	 * 获取角色名称
	 * @return
	 */
	public static String getRoleName() {
		String roleName = AuthUtil.getUserRole();
		return roleName;
	}

	/**
	 * 深拷贝
	 * @param entity 来源数据
	 * @param clazz 目标类
	 * @return
	 */
	public static <T> T copy(@Nullable Object entity, Class<T> clazz) {
		return BeanUtil.copy(entity, clazz);
	}

	/**
	 * 将字符串切割 切割默认符号 , 并获取第一个节点
	 */
	public static Long firstLong(String ids) {
		List<Long> result = Func.toLongList(ids);
		if (result.size() == 0) {
			return null;
		}
		return result.get(0);
	}

	/**
	 * 将字符串切割 切割默认符号 ，
	 * @param ids
	 * @return
	 */
	public static List<Long> toLongList(String ids) {
		return Func.toLongList(ids);
	}

	public static boolean isBlank(CharSequence str) {
		int length;
		if (str != null && (length = str.length()) != 0) {
			for(int i = 0; i < length; ++i) {
				if (!isBlankChar(str.charAt(i))) {
					return false;
				}
			}

			return true;
		} else {
			return true;
		}
	}

	public static boolean isBlankChar(int c) {
		return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == 65279 || c == 8234;
	}
}
