package org.springblade.modules.di.wrapper;

import org.springblade.common.cache.RoleCache;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.di.bean.entity.DiReport;
import org.springblade.modules.di.bean.vo.DiReportVO;
import org.springblade.modules.system.entity.Role;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 10:09
 * @Description:
 */
public class DiReportWrapper extends BaseEntityWrapper<DiReport, DiReportVO> {

	public static DiReportWrapper build() {
		return new DiReportWrapper();
	}

	@Override
	public DiReportVO entityVO(DiReport entity) {
		DiReportVO vo = entity == null ? null : CommonUtil.copy(entity, DiReportVO.class);
		Role role = RoleCache.getRole(Long.parseLong(vo.getDutyDept()));
		if (role != null) {
			vo.setDutyDept(role.getRoleName());
		}
		return vo;
	}
}
