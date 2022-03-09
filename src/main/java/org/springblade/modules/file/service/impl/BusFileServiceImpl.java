package org.springblade.modules.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.file.bean.entity.BusFile;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.file.mapper.BusFileMapper;
import org.springblade.modules.file.service.BusFileService;
import org.springblade.modules.file.wrapper.BusFileWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 14:16
 * @Description:
 */
@Service
public class BusFileServiceImpl extends ServiceImpl<BusFileMapper, BusFile> implements BusFileService {
	@Override
	public List<BusFileVO> getByIds(List<Long> fileIds) {
		if(fileIds.isEmpty()) {
			return new ArrayList<>();
		}

		LambdaQueryWrapper<BusFile> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(BusFile::getId, fileIds);
		return BusFileWrapper.build().listVO(list(wrapper));
	}
}
