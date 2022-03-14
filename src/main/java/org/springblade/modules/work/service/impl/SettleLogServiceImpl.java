package org.springblade.modules.work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.cache.UserCache;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.work.entity.bean.SettleLog;
import org.springblade.modules.work.enums.SettleBusType;
import org.springblade.modules.work.mapper.SettleLogMapper;
import org.springblade.modules.work.service.SettleLogService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 11:22
 * @Description:
 */
@Service
public class SettleLogServiceImpl extends ServiceImpl<SettleLogMapper, SettleLog> implements SettleLogService {
	@Override
	public void submitLog(String title, SettleBusType type) {
		SettleLog settleLog = new SettleLog();
		settleLog.setTitle(title);
		settleLog.setServiceType(type.getCode());
		settleLog.setBelongToDept(CommonUtil.getDeptId());
		settleLog.setBelongToUser(CommonUtil.getUserId());
		settleLog.setCreateTime(new Date());
		settleLog.setStatus(0);
		save(settleLog);
	}

	@Override
	public void finishLog(String title, SettleBusType type, Long userId) {
		SettleLog settleLog = new SettleLog();
		settleLog.setTitle(title);
		settleLog.setServiceType(type.getCode());
		settleLog.setBelongToDept(CommonUtil.firstLong(UserCache.getUser(userId).getDeptId()));
		settleLog.setBelongToUser(userId);
		settleLog.setCreateTime(new Date());
		settleLog.setStatus(2);
		save(settleLog);
	}

	@Override
	public void processLog(String title, SettleBusType type) {
		SettleLog settleLog = new SettleLog();
		settleLog.setTitle(title);
		settleLog.setServiceType(type.getCode());
		settleLog.setBelongToDept(CommonUtil.getDeptId());
		settleLog.setBelongToUser(CommonUtil.getUserId());
		settleLog.setCreateTime(new Date());
		settleLog.setStatus(1);
		save(settleLog);
	}
}
