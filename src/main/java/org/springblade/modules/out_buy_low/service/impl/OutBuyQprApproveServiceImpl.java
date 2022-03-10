package org.springblade.modules.out_buy_low.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprApproveVO;
import org.springblade.modules.out_buy_low.mapper.OutBuyQprApproveMapper;
import org.springblade.modules.out_buy_low.service.OutBuyQprApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/1 16:12
 * @Description:
 */
@Service
public class OutBuyQprApproveServiceImpl implements OutBuyQprApproveService {

	@Autowired
	private OutBuyQprApproveMapper approveMapper;

	@Override
	public IPage<OutBuyQprApproveVO> page(OutBuyQprApproveVO approveVO, IPage<OutBuyQprApproveVO> page) {
		return page.setRecords(approveMapper.page(approveVO, page));
	}
}
