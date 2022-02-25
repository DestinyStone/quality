package org.springblade.modules.di.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.di.bean.vo.DiAccountVO;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/25 11:28
 * @Description:
 */
public interface DiMapper{
	/**
	 * 台账分页
	 * @param vo
	 * @param page
	 * @return
	 */
	List<DiAccountVO> accountPage(@Param("vo") DiAccountVO vo, IPage<DiAccountVO> page);
}
