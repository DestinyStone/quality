package org.springblade.modules.work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.work.entity.bean.Message;
import org.springblade.modules.work.mapper.MessageMapper;
import org.springblade.modules.work.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:34
 * @Description:
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
}
