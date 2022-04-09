package org.springblade.common.core.anon;

import java.lang.annotation.*;

/**
 * @Author: xiaoxia
 * @Date: 2022/4/8 11:20
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotence {
	/**
	 * 限制时间
	 * @return
	 */
	int time() default 1000 * 5;

	/**
	 * 最大次数
	 * @return
	 */
	int count() default 1;
}
