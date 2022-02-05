package org.springblade.modules.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.bean.BpmProcessLog;
import org.springblade.modules.process.mapper.BpmProcessLogMapper;
import org.springblade.modules.process.service.BpmProcessLogService;
import org.springframework.stereotype.Service;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 14:07
 * @Description:
 */
@Service
public class BpmProcessLogServiceImpl extends ServiceImpl<BpmProcessLogMapper, BpmProcessLog> implements BpmProcessLogService {
	@Override
	public void convertAndSave(BpmProcess process) {
		BpmProcessLog log = new BpmProcessLog();
		log.setBpmId(process.getBpmId());
		log.setBusId(process.getBusId());
		log.setOperatorUser(CommonUtil.getUserId());
		log.setOperatorUserName(CommonUtil.getUserName());
		log.setOperatorDept(CommonUtil.getDeptId());
		log.setOperatorDeptPath(CommonUtil.getDeptPath());
		log.setOperatorRole(CommonUtil.getRoleName());
		log.setStartTime(process.getCreateTime());
		log.setEndTime(process.getEndTime());
		save(log);
	}
}
