package org.springblade.modules.check.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.AccessSaveCheckVO;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprApproveVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 18:51
 * @Description:
 */
public interface CheckService extends IService<Check> {
	/**
	 * 允许新增检查法的购件分页
	 * @param approveVO
	 * @param page
	 * @return
	 */
	IPage<AccessSaveCheckVO> accessSavePage(AccessSaveCheckVO approveVO, IPage<AccessSaveCheckVO> page);
}
