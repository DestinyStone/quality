package org.springblade.modules.file.wrapper;

import org.springblade.common.cache.SysCache;
import org.springblade.common.cache.UserCache;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.file.bean.entity.BusFile;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.system.entity.Dept;
import org.springblade.modules.system.entity.User;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 11:40
 * @Description:
 */
public class BusFileWrapper extends BaseEntityWrapper<BusFile, BusFileVO> {

	public static BusFileWrapper build() {
		return new BusFileWrapper();
	}

	@Override
	public BusFileVO entityVO(BusFile entity) {

		BusFileVO vo = entity == null ? null : CommonUtil.copy(entity, BusFileVO.class);
		User user = UserCache.getUser(vo.getCreateUser());
		vo.setCreateUserName(user == null ? "" : user.getName());

		Dept dept = SysCache.getDept(vo.getCreateDept());
		vo.setCreateDeptName(dept.getDeptName());

		return vo;
	}
}
