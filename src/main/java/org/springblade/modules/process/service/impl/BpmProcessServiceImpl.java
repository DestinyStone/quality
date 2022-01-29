package org.springblade.modules.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.process.core.ProcessContainer;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.mapper.BpmProcessMapper;
import org.springblade.modules.process.service.BpmProcessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/29 00:35
 * @Description:
 */
@Service
public class BpmProcessServiceImpl extends ServiceImpl<BpmProcessMapper, BpmProcess> implements BpmProcessService {
	@Override
	public void createTask(List<ProcessContainer> list) {
		ArrayList<BpmProcess> processes = new ArrayList<>();
		for (ProcessContainer item : list) {

		}
	}

	@Override
	public void pass(Long bpmId) {

	}
}
