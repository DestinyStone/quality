package org.springblade.modules.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.work.entity.bean.Message;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:34
 * @Description:
 */
public interface MessageService extends IService<Message> {
	/**
	 * 保存
	 * @param placeHolderReplace
	 */
	void save(String placeHolderReplace);
}
