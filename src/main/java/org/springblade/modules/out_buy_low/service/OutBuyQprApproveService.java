package org.springblade.modules.out_buy_low.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprApproveVO;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/1 16:12
 * @Description:
 */
public interface OutBuyQprApproveService {
	/**
	 * 自定义分页
	 * @param approveVO
	 * @param deptId
	 * @param page
	 * @return
	 */
	IPage<OutBuyQprApproveVO> page(OutBuyQprApproveVO approveVO, Long deptId, IPage<OutBuyQprApproveVO> page);
}
