package org.springblade.modules.code.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.code.bean.entity.BusCode;
import org.springblade.modules.code.mapper.BusCodeMapper;
import org.springblade.modules.code.service.BusCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 11:10
 * @Description:
 */
@Service
public class BusCodeServiceImpl extends ServiceImpl<BusCodeMapper, BusCode> implements BusCodeService {

	private static final Long DEFAULT_CODE = 10000L;
	private static final String DEFAULT_SEPARATE = "default";

	@Override
	@Transactional
	public synchronized String getCode(String flag) {
		LambdaQueryWrapper<BusCode> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BusCode::getFlag, flag);
		BusCode busCode = baseMapper.selectOne(wrapper);

		// 第一次新增
		if (busCode == null) {
			BusCode insert = new BusCode();
			insert.setCode(DEFAULT_CODE);
			insert.setUpdateTime(new Date());
			insert.setFlag(flag);
			insert.setSeparate(DEFAULT_SEPARATE);

			baseMapper.insert(insert);
			return DEFAULT_CODE + "";
		}

		BusCode update = new BusCode();
		update.setId(busCode.getId());
		update.setCode(busCode.getCode() + 1);
		update.setUpdateTime(new Date());
		baseMapper.updateById(update);
		return update.getCode() + "";
	}

}
