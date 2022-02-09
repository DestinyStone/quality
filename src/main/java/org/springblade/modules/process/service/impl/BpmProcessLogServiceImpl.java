package org.springblade.modules.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springblade.common.enums.ApproveNodeEnum;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.bean.BpmProcessLog;
import org.springblade.modules.process.enums.ApproveNodeStatusEnum;
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
		log.setOperatorTime(process.getOperatorTime());
		// 获取排序号
		log.setSort(getNextSort(log.getBusId()));

		if (ApproveNodeStatusEnum.BACK.getCode().equals(process.getBpmStatus())) {
			log.setOperatorResult(process.getBackCause());
			log.setOperatorStatus("已回答");
		}else {
			log.setOperatorStatus(convertNode(process.getBpmFlag()));
		}

		save(log);
	}

	private Integer getNextSort(String busId) {
		LambdaQueryWrapper<BpmProcessLog> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BpmProcessLog::getBusId, busId)
			.orderByDesc(BpmProcessLog::getSort)
			.last("limit 1");

		BpmProcessLog one = getOne(wrapper);
		if (one == null) {
			return 0;
		}
		return one.getSort() + 1;
	}

	private String convertNode(String flag) {
		ApproveNodeEnum nodeEnum = ApproveNodeEnum.getByMessage(flag);

		if (nodeEnum == null) {
			return "";
		}
		return nodeEnum.getOperatorEnum().getMessage();
	}
}
