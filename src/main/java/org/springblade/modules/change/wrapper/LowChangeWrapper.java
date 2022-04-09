package org.springblade.modules.change.wrapper;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springblade.common.cache.SysCache;
import org.springblade.common.cache.UserCache;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.change.bean.entity.LowChange;
import org.springblade.modules.change.bean.vo.LowChangeVO;
import org.springblade.modules.system.entity.Dept;
import org.springblade.modules.system.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:49
 * @Description:
 */
public class LowChangeWrapper extends BaseEntityWrapper<LowChange, LowChangeVO> {

	public final static HashMap<Integer, String> apparatusMap = new HashMap<>();
	public final static HashMap<Integer, String> productLineMap = new HashMap<>();
	public final static HashMap<Integer, String> processMap = new HashMap<>();
	public final static HashMap<Integer, String> utilMap = new HashMap<>();
	public final static HashMap<Integer, String> typeMap = new HashMap<>();
	public final static HashMap<Integer, String> mainChangeMap = new HashMap<>();

	static {
		apparatusMap.put(0, "TNGA1.5");
		apparatusMap.put(1, "TNGA2.0");
		apparatusMap.put(2, "NR");
		apparatusMap.put(3, "AZ");

		productLineMap.put(0, "TNGA#1");
		productLineMap.put(1, "TNGA#2");
		productLineMap.put(2, "TNGA#3");
		productLineMap.put(3, "TNGA#4");
		productLineMap.put(4, "TNGA#5");

		processMap.put(0, "TNGA1.5");
		processMap.put(1, "TNGA2.0");
		processMap.put(2, "NR");
		processMap.put(3, "AZ");

		utilMap.put(0, "MK设备");
		utilMap.put(1, "C设备");
		utilMap.put(2, "刀具");

		typeMap.put(0, "品质改善");
		typeMap.put(1, "成本降低");
		typeMap.put(2, "生产性提高");
		typeMap.put(3, "其他");

		mainChangeMap.put(0, "生成工序的新设或改善");
		mainChangeMap.put(1, "更换材料");
		mainChangeMap.put(2, "加工");
	}

	public static LowChangeWrapper build() {
		return new LowChangeWrapper();
	}

	@Override
	public LowChangeVO entityVO(LowChange entity) {
		LowChangeVO vo = entity == null ? null : CommonUtil.copy(entity, LowChangeVO.class);
		if (vo.getApparatusType() != null) {
			vo.setApparatusTypeName(apparatusMap.get(vo.getApparatusType()));
		}

		if (StrUtil.isNotBlank(vo.getProductLine())) {
			List<String> collect = Func.toIntList(vo.getProductLine()).stream().map(item -> {
				return productLineMap.get(item);
			}).collect(Collectors.toList());
			vo.setProductLineName(Func.join(collect));
		}

		if (vo.getProcessType() != null) {
			vo.setProcessName(apparatusMap.get(vo.getApparatusType()));
		}

		if (vo.getProcessType() != null) {
			vo.setProcessName(processMap.get(vo.getProcessType()));
		}

		if (vo.getUtilType() != null) {
			vo.setUtilName(utilMap.get(vo.getUtilType()));
		}

		if (StrUtil.isNotBlank(vo.getType())) {
			List<String> collect = Func.toIntList(vo.getType()).stream().map(item -> {
				return typeMap.get(item);
			}).collect(Collectors.toList());
			vo.setTypeName(Func.join(collect));
		}

		if (StrUtil.isNotBlank(vo.getMainChangeType())) {
			List<String> collect = Func.toIntList(vo.getMainChangeType()).stream().map(item -> {
				return mainChangeMap.get(item);
			}).collect(Collectors.toList());
			vo.setMainChangeTypeName(Func.join(collect));
		}

		if (vo.getCreateUser() != null) {
			User user = UserCache.getUser(vo.getCreateUser());
			vo.setCreateUserName(user == null ? "" : user.getName());
		}

		if (vo.getCreateDept() != null) {
			Dept dept = SysCache.getDept(vo.getCreateDept());
			vo.setCreateDeptName(dept == null ? "" : dept.getDeptName());
		}

		return vo;
	}
}
