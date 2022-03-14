package org.springblade.modules.process.wrapper;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springblade.common.cache.RoleCache;
import org.springblade.common.cache.SysCache;
import org.springblade.common.constant.ParamConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.process.entity.bean.BpmProcess;
import org.springblade.modules.process.entity.vo.BpmProcessVO;
import org.springblade.modules.system.entity.Dept;
import org.springblade.modules.system.entity.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/1 13:52
 * @Description:
 */
public class BpmProcessWrapper extends BaseEntityWrapper<BpmProcess, BpmProcessVO> {

	public static BpmProcessWrapper build() {
		return new BpmProcessWrapper();
	}

	@Override
	public BpmProcessVO entityVO(BpmProcess entity) {

		BpmProcessVO vo = entity == null ? null : CommonUtil.copy(entity, BpmProcessVO.class);
		if (StrUtil.isNotBlank(vo.getAccessDept())) {
			if ("-1".equals(vo.getAccessDept())) {
				vo.setAccessDeptName("禁止部门审批");
			}else if ("0".equals(vo.getAccessDept())) {
				vo.setAccessDeptName("任意部门");
			}else {
				List<Long> deptIds = Func.toLongList(vo.getAccessDept());
				ArrayList<String> deptName = new ArrayList<>();
				for (Long deptId : deptIds) {
					Dept dept = SysCache.getDept(deptId);
					deptName.add(dept == null ? "" : dept.getFullName());
				}
				vo.setAccessDeptName(Func.join(deptName));
			}
		}

		if (StrUtil.isNotBlank(vo.getAccessRole())) {
			if ("-1".equals(vo.getAccessRole())) {
				vo.setAccessRoleName("禁止角色审批");
			} else if ("0".equals(vo.getAccessRole())) {
				vo.setAccessRoleName("任意角色");
			} else if (ParamConstant.PROVIDER_REPLACE.equals(vo.getAccessRole())) {
				vo.setAccessRoleName("供应商");
			}
			else {
				List<Long> roleIds = Func.toLongList(vo.getAccessRole());
				ArrayList<String> roleName = new ArrayList<>();
				for (Long roleId : roleIds) {
					Role role = RoleCache.getRole(roleId);
					roleName.add(role == null ? "" : role.getRoleAlias());
				}
				vo.setAccessRoleName(Func.join(roleName));
			}
		}
		return vo;
	}
}
