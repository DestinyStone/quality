package org.springblade.modules.change.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.enums.ApproveStatusEnum;
import org.springblade.common.utils.CodeUtil;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.change.bean.dto.LowChangeDTO;
import org.springblade.modules.change.bean.entity.LowChange;
import org.springblade.modules.change.bean.vo.LowChangeQualityVO;
import org.springblade.modules.change.bean.vo.LowChangeVO;
import org.springblade.modules.change.service.LowChangeService;
import org.springblade.modules.change.wrapper.LowChangeWrapper;
import org.springblade.modules.file.service.BusFileService;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;
import org.springblade.modules.system.vo.UserVO;
import org.springblade.modules.system.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/31 10:05
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/change")
@RestController
@Api(value = "工变 审批接口", tags = "工变 审批接口")
public class LowChangeController {

	@Autowired
	private LowChangeService lowChangeService;

	@Autowired
	private BusFileService fileService;

	private static final String CODE_FLAG = "LOW_CHANGE";

	@Autowired
	private IUserService userService;

	/**
	 * 归口部门用户列表
	 */
	@GetMapping("/belong/user/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "列表")
	public R<IPage<UserVO>> belongUser(Query query, String searchKey, String exclude) {
		LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
		if (StrUtil.isNotBlank(searchKey)) {
			wrapper.like(User::getName, searchKey);
		}
		if (StrUtil.isNotBlank(exclude)) {
			List<Long> excludeIds = Func.toLongList(exclude);
			wrapper.notIn(User::getId, excludeIds);
		}
		IPage<User> page = userService.page(Condition.getPage(query), wrapper);
		return R.data(UserWrapper.build().pageVO(page));
	}

	/**
	 * 实施部门用户列表
	 */
	@GetMapping("/execution/user/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "列表")
	public R<IPage<UserVO>> executionUser(Query query, String exclude) {
		LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
		IPage<User> page = userService.page(Condition.getPage(query), wrapper);
		return R.data(UserWrapper.build().pageVO(page));
	}

	@PostMapping("/save")
	@ApiOperation("新增")
	public R save(@Valid @RequestBody LowChangeDTO changeDTO) {
		LowChange lowChange = BeanUtil.copy(changeDTO, LowChange.class);
		lowChange.setCode(CodeUtil.getCode(CODE_FLAG));
		lowChange.setCreateUser(CommonUtil.getUserId());
		lowChange.setCreateDept(CommonUtil.getDeptId());
		lowChange.setCreateTime(new Date());
		lowChange.setUpdateUser(CommonUtil.getUserId());
		lowChange.setUpdateTime(new Date());
		lowChange.setBpmStatus(ApproveStatusEnum.AWAIT.getCode());
		lowChange.setBpmNode(0);
		return R.status(lowChangeService.save(lowChange));
	}

	@PostMapping("/detail")
	@ApiOperation("详情")
	public R detail(@RequestParam("id") Long id) {
		LowChange lowChange = lowChangeService.getById(id);

		LowChangeVO lowChangeVO = LowChangeWrapper.build().entityVO(lowChange);
		String extendsFileIds = lowChange.getExtendsFileIds();
		if (StrUtil.isNotBlank(extendsFileIds)) {
			List<Long> fileIds = Func.toLongList(extendsFileIds);
			lowChangeVO.setExtendsFile(fileService.getByIds(fileIds));
		}

		return R.data(lowChangeVO);
	}

	@GetMapping("/page")
	@ApiOperation("分页")
	public R<IPage<LowChangeVO>> page(LowChangeVO checkVO, Query query) {
		LambdaQueryWrapper<LowChange> wrapper = new LambdaQueryWrapper<>();
		wrapper.and(StrUtil.isNotBlank(checkVO.getSearchKey()), item -> {
			item.like(LowChange::getBackground, checkVO.getSearchKey())
				.or()
				.like(LowChange::getChangeContent, checkVO.getSearchKey());
		});
		wrapper.eq(checkVO.getApparatusType() != null, LowChange::getApparatusType, checkVO.getApparatusType())
			.like(StrUtil.isNotBlank(checkVO.getProductLine()), LowChange::getProductLine, checkVO.getProductLine())
			.like(StrUtil.isNotBlank(checkVO.getType()), LowChange::getType, checkVO.getType());

		if (!new Integer(-1).equals(checkVO.getBpmStatusFilter())) {
			// 自测回
			if (new Integer(0).equals(checkVO.getBpmStatusFilter())) {
				wrapper.eq(LowChange::getBpmStatus, ApproveStatusEnum.SELF_BACK.getCode());
			}

			// 已驳回
			if (new Integer(1).equals(checkVO.getBpmStatusFilter())) {
				wrapper.eq(LowChange::getBpmStatus, ApproveStatusEnum.BACK.getCode());
			}

			// 进行中
			if (new Integer(2).equals(checkVO.getBpmStatusFilter())) {
				wrapper.and(item -> {
					item.eq(LowChange::getBpmStatus, ApproveStatusEnum.AWAIT.getCode())
						.or()
						.eq(LowChange::getBpmStatus, ApproveStatusEnum.PROCEED.getCode());
				});
			}

			// 已办结
			if (new Integer(3).equals(checkVO.getBpmStatusFilter())) {
				wrapper.eq(LowChange::getBpmStatus, ApproveStatusEnum.FINISN.getCode());
			}
		}
		IPage<LowChange> page = lowChangeService.page(Condition.getPage(query), wrapper);
		return R.data(LowChangeWrapper.build().pageVO(page));
	}

	@GetMapping("/quality")
	@ApiOperation("统计接口")
	public R<LowChangeQualityVO> quality() {
		LambdaQueryWrapper<LowChange> wrapper = new LambdaQueryWrapper<>();
		List<LowChange> list = lowChangeService.list(wrapper);

		LowChangeQualityVO qualityVO = new LowChangeQualityVO();
		qualityVO.setBack(0);
		qualityVO.setFinish(0);
		qualityVO.setProcess(0);
		qualityVO.setSelfBack(0);

		for (LowChange item : list) {
			if (item.getBpmStatus().equals(ApproveStatusEnum.SELF_BACK.getCode())) {
				qualityVO.setSelfBack(qualityVO.getSelfBack() + 1);
			}
			if (item.getBpmStatus().equals(ApproveStatusEnum.BACK.getCode())) {
				qualityVO.setBack(qualityVO.getBack() + 1);
			}
			if (item.getBpmStatus().equals(ApproveStatusEnum.AWAIT.getCode()) ||
				item.getBpmStatus().equals(ApproveStatusEnum.PROCEED.getCode())) {
				qualityVO.setProcess(qualityVO.getProcess() + 1);
			}
			if (item.getBpmStatus().equals(ApproveStatusEnum.FINISN.getCode())) {
				qualityVO.setFinish(qualityVO.getFinish() + 1);
			}
		}
		return R.data(qualityVO);
	}
}
