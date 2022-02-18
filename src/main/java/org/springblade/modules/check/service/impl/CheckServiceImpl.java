package org.springblade.modules.check.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.jnlp.CheckServicePermission;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.AccessSaveCheckVO;
import org.springblade.modules.check.mapper.CheckMapper;
import org.springblade.modules.check.service.CheckService;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprApproveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 18:51
 * @Description:
 */
@Service
public class CheckServiceImpl extends ServiceImpl<CheckMapper, Check> implements CheckService {

	@Autowired
	private CheckMapper checkMapper;

	@Override
	public IPage<AccessSaveCheckVO> accessSavePage(AccessSaveCheckVO approveVO, IPage<AccessSaveCheckVO> page) {
		return page.setRecords(checkMapper.accessSavePage(approveVO, page));
	}
}
