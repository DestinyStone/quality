package org.springblade.modules.work.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.work.entity.bean.Message;
import org.springblade.modules.work.entity.vo.MessageVO;
import org.springblade.modules.work.service.MessageService;
import org.springblade.modules.work.wrapper.MessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:36
 * @Description: 站内消息 接口
 */
@RequestMapping(RootMappingConstant.root + "/message")
@RestController
@Api(value = "站内消息 接口", tags = "站内消息 接口")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@GetMapping("/page")
	public R<IPage<MessageVO>> page(MessageVO messageVO,  Query query) {
		LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
		wrapper.orderByDesc(Message::getCreateTime);
		IPage<Message> page = messageService.page	(Condition.getPage(query), wrapper);
		return R.data(MessageWrapper.build().pageVO(page));
	}
}
