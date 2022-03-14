package org.springblade.modules.work.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.check.bean.vo.CheckApproveQualityVO;
import org.springblade.modules.check.service.CheckService;
import org.springblade.modules.di.bean.vo.DiApproveQualityVO;
import org.springblade.modules.di.service.DiReportApproveService;
import org.springblade.modules.process_low.bean.vo.ProcessLowApproveQualityVO;
import org.springblade.modules.process_low.service.ProcessLowService;
import org.springblade.modules.work.entity.bean.Message;
import org.springblade.modules.work.entity.bean.SettleLog;
import org.springblade.modules.work.entity.vo.EventVO;
import org.springblade.modules.work.entity.vo.MessageVO;
import org.springblade.modules.work.entity.vo.SettleLogVO;
import org.springblade.modules.work.service.MessageService;
import org.springblade.modules.work.service.SettleLogService;
import org.springblade.modules.work.wrapper.MessageWrapper;
import org.springblade.modules.work.wrapper.SettleLogWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:36
 * @Description: 站内消息 接口
 */
@RequestMapping(RootMappingConstant.root + "/work")
@RestController
@Api(value = "站内消息 接口", tags = "站内消息 接口")
public class WorkController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private ProcessLowService lowService;

	@Autowired
	private CheckService checkService;

	@Autowired
	private DiReportApproveService diService;

	@Autowired
	private SettleLogService settleLogService;

	@GetMapping("/event")
	public R<Map<String, EventVO>> event() {
		EventVO lowEventVo = EventVO.getInit();
		ProcessLowApproveQualityVO lowQuality = lowService.quality();
		lowEventVo.setQuality(lowQuality.getAwait());
		lowEventVo.setUrgeQuality(lowQuality.getStaleDated());

		EventVO checkEventVO = EventVO.getInit();
		CheckApproveQualityVO checkQuality = checkService.quality();
		checkEventVO.setQuality(checkQuality.getAwait());
		checkEventVO.setUrgeQuality(checkQuality.getStaleDated());

		EventVO diEventVO = EventVO.getInit();
		DiApproveQualityVO diQuality = diService.quality();
		diEventVO.setQuality(diQuality.getAwait());
		diEventVO.setUrgeQuality(diQuality.getStaleDated());

		HashMap<String, EventVO> map = new HashMap<>();
		map.put("low", lowEventVo);
		map.put("check", checkEventVO);
		map.put("di", diEventVO);
		return R.data(map);
	}

	@GetMapping("/settle/log")
	public R<IPage<SettleLogVO>> settleLog(Query query) {
		// 获取前30天时间
		Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 30L);
		LambdaQueryWrapper<SettleLog> wrapper = new LambdaQueryWrapper<>();
		wrapper.ge(SettleLog::getCreateTime, date);
		if (!CommonUtil.isAdmin()) {
			wrapper.eq(SettleLog::getBelongToUser, CommonUtil.getUserId());
		}

		IPage<SettleLog> page = settleLogService.page(Condition.getPage(query), wrapper);
		return R.data(SettleLogWrapper.build().pageVO(page));
	}

	@GetMapping("/message/page")
	public R<IPage<MessageVO>> page(MessageVO messageVO,  Query query) {
		LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
		wrapper.orderByDesc(Message::getCreateTime);
		IPage<Message> page = messageService.page	(Condition.getPage(query), wrapper);
		return R.data(MessageWrapper.build().pageVO(page));
	}
}
