package org.springblade.modules.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.code.bean.entity.BusCode;

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

}
