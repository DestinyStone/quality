package org.springblade.modules.out_buy_low.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.enums.ApproveStatusEmun;
import org.springblade.common.utils.CodeUtil;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.out_buy_low.bean.dto.OutBuyQprDTO;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprVO;
import org.springblade.modules.out_buy_low.service.OutBuyQprService;
import org.springblade.modules.out_buy_low.wrapper.OutBuyQprWrapper;
import org.springblade.modules.process_low.bean.dto.ProcessLowDTO;
import org.springblade.modules.process_low.bean.entity.ProcessLow;
import org.springblade.modules.process_low.bean.vo.ProcessLowVO;
import org.springblade.modules.process_low.wrapper.ProcessLowWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 10:05
 * @Description:
 */
@RequestMapping(RootMappingConstant.root + "/out/buy/qpr")
@RestController
@Api(value = "工序内不良接口", tags = "工序内不良接口")
public class OutBuyQprController {

	private static final String CODE_FLAG = "OUT_BUY_QPR";

	@Autowired
	private OutBuyQprService qprService;

	@PostMapping("/save")
	@ApiOperation("新增")
	public R save(@Valid @RequestBody OutBuyQprDTO qprDTO) {
		OutBuyQpr qpr = BeanUtil.copy(qprDTO, OutBuyQpr.class);
		qpr.setCreateUser(CommonUtil.getUserId());
		qpr.setCreateTime(new Date());
		qpr.setCreateDept(CommonUtil.getDeptId());
		qpr.setUpdateUser(CommonUtil.getUserId());
		qpr.setUpdateTime(new Date());
		qpr.setBpmStatus(0);
		qpr.setCode(CodeUtil.getCode(CODE_FLAG));
		return R.status(qprService.save(qpr));
	}

	@GetMapping("/page")
	@ApiOperation("分页")
	public R<IPage<OutBuyQprVO>> page(OutBuyQprVO qprVO, Query query) {
		LambdaQueryWrapper<OutBuyQpr> wrapper = new LambdaQueryWrapper<>();
		wrapper.and(StrUtil.isNotBlank(qprVO.getSearchKey()), item -> {
			item.like(OutBuyQpr::getTitle, qprVO.getSearchKey())
				.or()
				.like(OutBuyQpr::getEventRemark, qprVO.getEventRemark())
				.or()
				.like(OutBuyQpr::getDutyDept, qprVO.getDutyDept());
		});

		if (!new Integer(-1).equals(qprVO.getBpmStatusFilter())) {
			// 自测回
			if (new Integer(0).equals(qprVO.getBpmStatusFilter())) {
				wrapper.eq(OutBuyQpr::getBpmStatus, ApproveStatusEmun.SELF_BACK.getCode());
			}

			// 已驳回
			if (new Integer(1).equals(qprVO.getBpmStatusFilter())) {
				wrapper.eq(OutBuyQpr::getBpmStatus, ApproveStatusEmun.BACK.getCode());
			}

			// 进行中
			if (new Integer(2).equals(qprVO.getBpmStatusFilter())) {
				wrapper.and(item -> {
					item.eq(OutBuyQpr::getBpmStatus, ApproveStatusEmun.AWAIT.getCode())
						.or()
						.eq(OutBuyQpr::getBpmStatus, ApproveStatusEmun.PROCEED.getCode());
				});
			}

			// 已办结
			if (new Integer(3).equals(qprVO.getBpmStatusFilter())) {
				wrapper.eq(OutBuyQpr::getBpmStatus, ApproveStatusEmun.FINISN.getCode());
			}
		}

		wrapper.eq(qprVO.getTriggerAddress() != null, OutBuyQpr::getTriggerAddress, qprVO.getTriggerAddress());

		IPage<OutBuyQpr> page = qprService.page(Condition.getPage(query), wrapper);
		return R.data(OutBuyQprWrapper.build().pageVO(page));
	}
}
