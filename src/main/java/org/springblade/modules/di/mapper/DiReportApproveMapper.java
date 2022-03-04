package org.springblade.modules.di.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.di.bean.vo.DiReportApproveVO;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 16:09
 * @Description:
 */
public interface DiReportApproveMapper {
	/**
	 * 分页
	 * @param vo
	 * @param deptId
	 * @param page
	 * @return
	 */
	List<DiReportApproveVO> page(@Param("vo")DiReportApproveVO vo, @Param("deptId") Long deptId, IPage<DiReportApproveVO> page);
}
