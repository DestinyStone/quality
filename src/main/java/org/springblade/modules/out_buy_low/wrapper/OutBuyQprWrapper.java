package org.springblade.modules.out_buy_low.wrapper;

import org.springblade.common.cache.RoleCache;
import org.springblade.common.cache.UserCache;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprVO;
import org.springblade.modules.system.entity.Role;
import org.springblade.modules.system.entity.User;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 10:09
 * @Description:
 */
public class OutBuyQprWrapper extends BaseEntityWrapper<OutBuyQpr, OutBuyQprVO> {

	public static OutBuyQprWrapper build() {
		return new OutBuyQprWrapper();
	}

	@Override
	public OutBuyQprVO entityVO(OutBuyQpr entity) {

		OutBuyQprVO vo = entity == null ? null : CommonUtil.copy(entity, OutBuyQprVO.class);
		User user = UserCache.getUser(vo.getCreateUser());
		vo.setCreateUserName(user == null ? "" : user.getName());
		Role role = RoleCache.getRole(Long.parseLong(vo.getDutyDept()));
		if (role != null) {
			vo.setDutyDept(role.getRoleName());
		}
		return vo;
	}
}
