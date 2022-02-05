package org.springblade.modules.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.process.entity.bean.BpmProcessUrge;
import org.springblade.modules.process.mapper.BpmProcessUrgeMapper;
import org.springblade.modules.process.service.ProcessUrgeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 12:03
 * @Description:
 */
@Service
public class ProcessUrgeServiceImpl extends ServiceImpl<BpmProcessUrgeMapper, BpmProcessUrge> implements ProcessUrgeService {
	@Override
	public List<BpmProcessUrge> getByBpmIds(List<Long> bpmIds) {
		LambdaQueryWrapper<BpmProcessUrge> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(BpmProcessUrge::getBpmId, bpmIds);

		return list(wrapper);
	}
}
