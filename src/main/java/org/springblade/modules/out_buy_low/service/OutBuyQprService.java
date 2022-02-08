package org.springblade.modules.out_buy_low.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 10:03
 * @Description:
 */
public interface OutBuyQprService extends IService<OutBuyQpr> {
	/**
	 * 新增， 并激活任务
	 * @param qpr
	 * @return
	 */
	Boolean saveAndActiveTask(OutBuyQpr qpr);

	/**
	 * 新增， 不激活任务
	 */
	Boolean saveUnActiveTask(OutBuyQpr qpr);
}