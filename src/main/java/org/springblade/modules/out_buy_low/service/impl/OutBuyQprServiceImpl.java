package org.springblade.modules.out_buy_low.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CodeUtil;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;
import org.springblade.modules.out_buy_low.mapper.OutBuyQprMapper;
import org.springblade.modules.out_buy_low.service.OutBuyQprService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 10:04
 * @Description:
 */
@Service
public class OutBuyQprServiceImpl extends ServiceImpl<OutBuyQprMapper, OutBuyQpr> implements OutBuyQprService {

	private static final String CODE_FLAG = "LOW";

	@Override
	public Boolean saveAndActiveTask(OutBuyQpr qpr) {
		commonSet(qpr);
		qpr.setProcessLowFlag(0);
		boolean status = save(qpr);
		ApproveUtils.createTask(qpr.getId() + "", ApproveUtils.ApproveLinkEnum.OUT_BUY_LOW);
		return status;
	}

	@Override
	@Transactional
	public Boolean saveUnActiveTask(OutBuyQpr qpr) {
		OutBuyQpr outBuyQpr = getById(qpr.getId());
		if (outBuyQpr != null) {
			this.removeById(qpr.getId());
		}

		commonSet(qpr);
		qpr.setProcessLowFlag(2);
		return this.save(qpr);
	}

	@Override
	public List<OutBuyQpr> getByIds(List<Long> qprIds) {
		LambdaQueryWrapper<OutBuyQpr> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(OutBuyQpr::getId, qprIds);
		return list(wrapper);
	}

	public void commonSet(OutBuyQpr qpr) {
		qpr.setCreateUser(CommonUtil.getUserId());
		qpr.setCreateTime(new Date());
		qpr.setCreateDept(CommonUtil.getDeptId());
		qpr.setUpdateUser(CommonUtil.getUserId());
		qpr.setUpdateTime(new Date());
		qpr.setBpmStatus(0);
		qpr.setCode(CodeUtil.getCode(CODE_FLAG));
	}
}
