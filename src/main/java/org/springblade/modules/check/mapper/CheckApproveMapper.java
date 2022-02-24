package org.springblade.modules.check.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.check.bean.vo.CheckApproveVO;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/23 18:29
 * @Description:
 */
public interface CheckApproveMapper {
	/**
	 * 审批分页
	 * @param approveVO
	 * @param deptId
	 * @param page
	 * @return
	 */
	List<CheckApproveVO> page(@Param("vo") CheckApproveVO approveVO, @Param("deptId") Long deptId, IPage<CheckApproveVO> page);
}
