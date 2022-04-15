package org.springblade.modules.out_buy_low.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.common.utils.ApproveUtils;
import org.springblade.common.utils.CodeUtil;
import org.springblade.common.utils.CommonUtil;
import org.springblade.modules.file.bean.entity.BusFile;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.file.service.BusFileService;
import org.springblade.modules.file.wrapper.BusFileWrapper;
import org.springblade.modules.out_buy_low.bean.entity.OutBuyQpr;
import org.springblade.modules.out_buy_low.bean.vo.OutBuyQprVO;
import org.springblade.modules.out_buy_low.mapper.OutBuyQprMapper;
import org.springblade.modules.out_buy_low.service.OutBuyQprService;
import org.springblade.modules.out_buy_low.utils.OutBuyQprEmailUtils;
import org.springblade.modules.out_buy_low.wrapper.OutBuyQprWrapper;
import org.springblade.modules.work.enums.SettleBusType;
import org.springblade.modules.work.service.SettleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 10:04
 * @Description:
 */
@Service
public class OutBuyQprServiceImpl extends ServiceImpl<OutBuyQprMapper, OutBuyQpr> implements OutBuyQprService {

	private static final String CODE_FLAG = "LOW";

	@Autowired
	private SettleLogService settleLogService;

	@Autowired
	private BusFileService fileService;

	@Override
	@Transactional
	public Boolean saveAndActiveTask(OutBuyQpr qpr) {
		commonSet(qpr);
		qpr.setProcessLowFlag(0);
		boolean status = save(qpr);
		ApproveUtils.createTask(qpr.getId() + "", ApproveUtils.ApproveLinkEnum.OUT_BUY_LOW);
		settleLogService.submitLog(qpr.getTitle(), SettleBusType.OUT_LOW);
		OutBuyQprEmailUtils.sendWarningEmail(qpr);
		return status;
	}

	@Override
	@Transactional
	public Boolean saveUnActiveTask(OutBuyQpr qpr) {
		OutBuyQpr outBuyQpr = getById(qpr.getId());
		if (outBuyQpr != null) {
			this.removeById(qpr.getId());
		}

		commonSet(qpr);
		qpr.setProcessLowFlag(2);
		return this.save(qpr);
	}

	@Override
	public List<OutBuyQpr> getByIds(List<Long> qprIds) {
		LambdaQueryWrapper<OutBuyQpr> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(OutBuyQpr::getId, qprIds);
		return list(wrapper);
	}

	@Override
	public OutBuyQprVO getDetail(Long id) {
		OutBuyQpr outBuyQpr = getById(id);
		OutBuyQprVO outBuyQprVO = OutBuyQprWrapper.build().entityVO(outBuyQpr);

		ArrayList<Long> fileIds = new ArrayList<>();
		List<Long> extendsFileIds = CommonUtil.toLongList(outBuyQpr.getAnalyseExtendsFileIds());
		List<Long> imgReportIds = CommonUtil.toLongList(outBuyQpr.getImgReportIds());

		fileIds.addAll(extendsFileIds);
		fileIds.addAll(imgReportIds);

		if (fileIds.isEmpty()) {
			return outBuyQprVO;
		}

		LambdaQueryWrapper<BusFile> fileWrapper = new LambdaQueryWrapper<>();
		fileWrapper.in(BusFile::getId, fileIds);
		List<BusFileVO> list = BusFileWrapper.build().listVO(fileService.list(fileWrapper));

		outBuyQprVO.setAnalyseExtendsFileList(new ArrayList<>());
		outBuyQprVO.setImgReportFiles(new ArrayList<>());

		for (BusFileVO item : list) {
			if(extendsFileIds.contains(item.getId())) {
				outBuyQprVO.getAnalyseExtendsFileList().add(item);
			}

			if(imgReportIds.contains(item.getId())) {
				outBuyQprVO.getImgReportFiles().add(item);
			}
		}
		return outBuyQprVO;
	}

	public void commonSet(OutBuyQpr qpr) {
		qpr.setCreateUser(CommonUtil.getUserId());
		qpr.setCreateTime(new Date());
		qpr.setCreateDept(CommonUtil.getDeptId());
		qpr.setUpdateUser(CommonUtil.getUserId());
		qpr.setUpdateTime(new Date());
		qpr.setBpmStatus(0);
		qpr.setCode(CodeUtil.getCode(CODE_FLAG));
	}
}
