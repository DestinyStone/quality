package org.springblade.modules.check.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.AccessSaveCheckVO;
import org.springblade.modules.check.bean.vo.CheckApproveQualityVO;
import org.springblade.modules.check.bean.vo.CheckVO;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 18:51
 * @Description:
 */
public interface CheckService extends IService<Check> {
	/**
	 * 允许新增检查法的购件分页
	 * @param approveVO
	 * @param page
	 * @return
	 */
	IPage<AccessSaveCheckVO> accessSavePage(AccessSaveCheckVO approveVO, IPage<AccessSaveCheckVO> page);

	/**
	 *
	 * @param checkVO
	 * @param page
	 * @return
	 */
	IPage<CheckVO> customPage(CheckVO checkVO, IPage<CheckVO> page);

	/**
	 * 批量新增并激活任务
	 * @param collect
	 * @return
	 */
	Boolean saveBatchAndActiveTask(List<Check> collect);

	/**
	 * 新增并激活任务
	 * @param check
	 * @return
	 */
	Boolean saveAndActiveTask(Check check);

	/**
	 * 统计
	 * @return
	 */
	CheckApproveQualityVO quality();
}
