package org.springblade.modules.process_low.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.process_low.bean.vo.ProcessLowApproveVO;

import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/30 22:29
 * @Description:
 */
public interface ProcessLowApproveMapper {
	/**
	 * 自定义分页查询
	 * @param processLowVO
	 * @param deptId
	 * @param page
	 * @return
	 */
	List<ProcessLowApproveVO> page(@Param("vo") ProcessLowApproveVO processLowVO, @Param("deptId") Long deptId, IPage<ProcessLowApproveVO> page);
}
