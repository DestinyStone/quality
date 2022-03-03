package org.springblade.modules.di.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.di.bean.vo.DiAccountVO;
import org.springblade.modules.di.mapper.DiMapper;
import org.springblade.modules.di.service.DiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/25 11:28
 * @Description:
 */
@Service
public class DiServiceImpl implements DiService {

	@Autowired
	private DiMapper diMapper;

	@Override
	public IPage<DiAccountVO> accountPage(DiAccountVO vo, IPage<DiAccountVO> page) {
		return page.setRecords(diMapper.accountPage(vo, page));
	}
}
