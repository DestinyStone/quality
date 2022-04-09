package org.springblade.common.core.aop;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springblade.common.core.anon.Idempotence;
import org.springblade.common.utils.CommonUtil;
import org.springblade.common.utils.RedisUtils;
import org.springblade.core.log.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xiaoxia
 * @Date: 2022/4/8 11:23
 * @Description:
 */
@Component
@Aspect
public class IdempotenceAOP {

	private static final String CACHE_NAME = "IDEMPOTENCE";
	private static final String TEMPLATE_CACHE = "idempotence:${method}:${host}";

	@Pointcut("@annotation(org.springblade.common.core.anon.Idempotence)")
	public void point() {}

	@Before("point()")
	public void before(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		String key = getKey(signature);
		validate(key, getPointcutAnnon(joinPoint));
	}

	private String getKey(Signature signature) {
		Map<String, String> map = new HashMap<>();
		map.put("method", signature.getDeclaringTypeName() + "." + signature.getName());
		map.put("host", CommonUtil.getHost());
		return CommonUtil.placeHolderReplace(TEMPLATE_CACHE, map);
	}

	private void validate(String key, Idempotence idempotence) {
		String result = RedisUtils.get(key, String.class);
		Integer quality = 0;
		if (StrUtil.isNotBlank(result)) {
			quality = new Integer(result);
		}
		if (quality >= idempotence.count()) {
			throw new ServiceException("操作频繁, 请稍后再试");
		}

		RedisUtils.set(key, quality + 1 + "", new Long(idempotence.time() + ""));
	}

	private Idempotence getPointcutAnnon(JoinPoint point) {
		Object[] args = point.getArgs();
		Class<?>[] argTypes = new Class[point.getArgs().length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		Method method = null;
		try {
			method = point.getTarget().getClass()
				.getMethod(point.getSignature().getName(), argTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return method.getAnnotation(Idempotence.class);
	}

}
