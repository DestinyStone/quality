package org.springblade.modules.check.wrapper;

import org.springblade.common.cache.SysCache;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.check.bean.entity.Check;
import org.springblade.modules.check.bean.vo.CheckDetailVO;
import org.springblade.modules.system.entity.Dept;
import org.springblade.modules.work.entity.bean.Message;
import org.springblade.modules.work.entity.vo.MessageVO;
import sun.net.www.protocol.http.AuthCache;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:49
 * @Description:
 */
public class CheckWrapper extends BaseEntityWrapper<Check, CheckDetailVO> {

	public static CheckWrapper build() {
		return new CheckWrapper();
	}

	@Override
	public CheckDetailVO entityVO(Check entity) {
		CheckDetailVO vo = entity == null ? null : CommonUtil.copy(entity, CheckDetailVO.class);
		Dept dept = SysCache.getDept(vo.getCreateDept());
		vo.setCreateDeptName(dept == null ? "" : dept.getDeptName());
		return vo;
	}
}
