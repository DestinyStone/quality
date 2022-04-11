package org.springblade.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Author: xiaoxia
 * @Date: 2022/4/8 16:28
 * @Description:
 */
public class RedisUtils {

	private static StringRedisTemplate redisTemplate;

	static {
		redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
	}

	public static <T> void set(String key, T value, Long time) {
		if (value instanceof String) {
			redisTemplate.opsForValue().set(key, value.toString(), time, TimeUnit.MILLISECONDS);
			return;
		}
		redisTemplate.opsForValue().set(key, JSONObject.toJSONString(value), time, TimeUnit.MILLISECONDS);
	}

	public static <T> T get(String key, Class<T> clazz) {
		String result = redisTemplate.opsForValue().get(key);
		if (StrUtil.isBlank(result)) {
			return null;
		}
		if (clazz == String.class) {
			return (T)result;
		}
		return JSONObject.parseObject(result, clazz);
	}
}
