package org.springblade.modules.work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.work.entity.bean.Message;
import org.springblade.modules.work.mapper.MessageMapper;
import org.springblade.modules.work.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:34
 * @Description:
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

	@Override
	public void save(String placeHolderReplace) {
		Message message = new Message();
		message.setTitle(placeHolderReplace);
		message.setCreateTime(new Date());
		message.setBelongToDept(CommonUtil.getDeptId());
		message.setBelongToUser(CommonUtil.getUserId());
		message.setType(0);
		save(message);
	}
}
