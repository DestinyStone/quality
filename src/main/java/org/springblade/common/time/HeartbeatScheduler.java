package org.springblade.common.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.modules.system.mapper.HeartbeatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @Author: DestinyStone
 * @Date: 2022/3/13 11:16
 * @Description:
 */
@Component
public class HeartbeatScheduler {

	@Autowired
	private HeartbeatMapper heartbeatMapper;

	private final Logger logger = LoggerFactory.getLogger(HeartbeatScheduler.class);

	/**
	 * 每30秒执行1次
	 */
	@Scheduled(cron = "0/15 * * * * ? ")
	public void task() {
		try {
			heartbeatMapper.heartbeat();
			logger.info("连接数据库正常");
		}catch (Exception e) {
			logger.error("连接数据库异常");
			e.printStackTrace();
		}
	}
}
