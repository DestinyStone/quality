package org.springblade.modules.work.wrapper;

import org.springblade.common.cache.UserCache;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.bean.vo.ProcessLowVO;
import org.springblade.modules.process_low.wrapper.ProcessLowWrapper;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.work.entity.bean.Message;
import org.springblade.modules.work.entity.vo.MessageVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:49
 * @Description:
 */
public class MessageWrapper extends BaseEntityWrapper<Message, MessageVO> {

	public static MessageWrapper build() {
		return new MessageWrapper();
	}

	@Override
	public MessageVO entityVO(Message entity) {
		MessageVO vo = entity == null ? null : CommonUtil.copy(entity, MessageVO.class);
		return vo;
	}
}
