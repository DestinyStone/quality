package org.springblade.modules.codeing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.codeing.bean.entity.BusCode;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 11:09
 * @Description:
 */
public interface BusCodeService extends IService<BusCode> {

	/**
	 * 获取唯一码
	 * @param flag
	 * @return
	 */
	String getCode(String flag);

	/**
	 * 分隔
	 * @param separate
	 * @param flag
	 * @return
	 */
	BusCode getBySeparate(String separate, String flag);
}
