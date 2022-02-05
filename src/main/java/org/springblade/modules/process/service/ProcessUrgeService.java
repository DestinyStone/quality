package org.springblade.modules.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.process.entity.bean.BpmProcessUrge;

import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 12:03
 * @Description:
 */
public interface ProcessUrgeService extends IService<BpmProcessUrge> {
	/**
	 * 根据 bpmId 获取
	 * @param bpmIds
	 * @return
	 */
	List<BpmProcessUrge> getByBpmIds(List<Long> bpmIds);
}
