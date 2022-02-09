package org.springblade.modules.process.wrapper;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springblade.common.constant.DateConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.process.entity.bean.BpmProcessLog;
import org.springblade.modules.process.entity.vo.BpmProcessLogVO;

import java.util.Date;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 14:58
 * @Description:
 */
public class BpmProcessLogWrapper  extends BaseEntityWrapper<BpmProcessLog, BpmProcessLogVO> {

	public static BpmProcessLogWrapper build() {
		return new BpmProcessLogWrapper();
	}

	@Override
	public BpmProcessLogVO entityVO(BpmProcessLog entity) {
		BpmProcessLogVO vo = entity == null ? null : CommonUtil.copy(entity, BpmProcessLogVO.class);
		vo.setIntervalTime(computedIntervalTime(vo.getStartTime(), vo.getOperatorTime()));
		return vo;
	}

	public String computedIntervalTime(Date startTime, Date operatorTime) {
		if (startTime == null || operatorTime == null) {
			return "";
		}

		Long intervalTime = operatorTime.getTime() - startTime.getTime();
		String result = "";
		if (intervalTime >= DateConstant.DAYS_TIME) {
			long days = intervalTime / DateConstant.DAYS_TIME;
			intervalTime %= DateConstant.DAYS_TIME;
			result += days + "天";
		}
		if (intervalTime >= DateConstant.HOURS_TIME) {
			long hours = intervalTime / DateConstant.HOURS_TIME;
			intervalTime %= DateConstant.HOURS_TIME;
			result += hours + "小时";
		}

		if (intervalTime >= DateConstant.MIN_TIME) {
			long min = intervalTime / DateConstant.MIN_TIME;
			intervalTime %= DateConstant.MIN_TIME;
			result += min + "分";
		}

		if (intervalTime >= DateConstant.SECOND_TIME) {
			long second = intervalTime / DateConstant.SECOND_TIME;
			result += second + "秒";
		}

		if (StrUtil.isBlank(result)) {
			return "0秒";
		}
		return result;
	}
}
