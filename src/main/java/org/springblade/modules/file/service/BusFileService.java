package org.springblade.modules.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.file.bean.entity.BusFile;
import org.springblade.modules.file.bean.vo.BusFileVO;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 14:16
 * @Description:
 */
public interface BusFileService extends IService<BusFile> {
	/**
	 * 获取文件
	 * @param fileIds
	 * @return
	 */
	List<BusFileVO> getByIds(List<Long> fileIds);
}
