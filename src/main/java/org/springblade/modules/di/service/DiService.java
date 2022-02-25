package org.springblade.modules.di.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.di.bean.vo.DiAccountVO;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/25 11:28
 * @Description:
 */
public interface DiService {
	/**
	 * 台账分页
	 * @param vo
	 * @param page
	 * @return
	 */
	IPage<DiAccountVO> accountPage(DiAccountVO vo, IPage<DiAccountVO> page);
}
