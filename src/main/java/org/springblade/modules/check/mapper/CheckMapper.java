package org.springblade.modules.check.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.AccessSaveCheckVO;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprApproveVO;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 18:50
 * @Description:
 */
public interface CheckMapper extends BaseMapper<Check> {
	/**
	 * 允许新增的检查法分页
	 * @param approveVO
	 * @param page
	 * @return
	 */
	List<AccessSaveCheckVO> accessSavePage(@Param("vo") AccessSaveCheckVO approveVO, IPage<AccessSaveCheckVO> page);
}
