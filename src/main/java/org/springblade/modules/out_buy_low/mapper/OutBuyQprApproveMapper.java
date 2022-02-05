package org.springblade.modules.out_buy_low.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprApproveVO;

import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/1 16:10
 * @Description:
 */
public interface OutBuyQprApproveMapper {
	/**
	 * 自定义分页查询
	 * @param processLowVO
	 * @param deptId
	 * @param page
	 * @return
	 */
	List<OutBuyQprApproveVO> page(@Param("vo") OutBuyQprApproveVO processLowVO, @Param("deptId") Long deptId, IPage<OutBuyQprApproveVO> page);
}
