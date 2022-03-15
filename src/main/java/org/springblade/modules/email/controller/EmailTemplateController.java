package org.springblade.modules.email.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.email.bean.entity.EmailTemplate;
import org.springblade.modules.email.service.IEmailTemplateParamService;
import org.springblade.modules.email.service.IEmailTemplateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/15 14:16
 * @Description:
 */
@RestController
@AllArgsConstructor
@RequestMapping(RootMappingConstant.root + "/email-template")
@Api(value = "邮件模板", tags = "邮件模板")
public class EmailTemplateController {

	private IEmailTemplateService emailTemplateService;

	private IEmailTemplateParamService emailTemplateParamService;

//	/**
//	 * 详情
//	 */
//	@GetMapping("/detail")
//	@ApiOperationSupport(order = 1)
//	@ApiOperation(value = "详情", notes = "传入template")
//	public R<EmailTemplateDTO> detail(EmailTemplate template) {
//		EmailTemplate detail = emailTemplateService.getOne(Condition.getQueryWrapper(template));
//		if (detail == null) {
//			throw new ServiceException("邮件模板不存在");
//		}
//		EmailTemplateDTO result = Objects.requireNonNull(BeanUtil.copy(detail, EmailTemplateDTO.class));
//		result.setParams(emailTemplateParamService.getParamsByTemplateId(detail.getId()));
//		return R.data(result);
//	}

	/**
	 * 分页 邮件模板
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入template")
	public R<IPage<EmailTemplate>> list(EmailTemplate template, Query query) {
		LambdaQueryWrapper<EmailTemplate> wrapper = Wrappers.<EmailTemplate>lambdaQuery();
		wrapper.select(EmailTemplate::getId, EmailTemplate::getTitle, EmailTemplate::getCode, EmailTemplate::getStatus);
		wrapper.eq(template.getStatus() != null, EmailTemplate::getStatus, template.getStatus())
			.like(!StrUtil.isEmpty(template.getTitle()), EmailTemplate::getTitle, template.getTitle());
		IPage<EmailTemplate> pages = emailTemplateService.page(Condition.getPage(query), wrapper);
		return R.data(pages);
	}

//	/**
//	 * 新增/更新邮件模板
//	 *
//	 * @return
//	 */
//	@PostMapping("submit")
//	@ApiOperationSupport(order = 3)
//	@ApiOperation(value = "上传接口", notes = "传入文件")
//	public R submit(@Valid @RequestBody EmailTemplateDTO template) {
//		template.setContent(Base64.decodeStr(template.getContent()));
//		EmailTemplate oldTemplate = emailTemplateService.getTemplateByCode(template.getCode(), template.getId());
//		if (oldTemplate != null) {
//			throw new ServiceException("邮件模板已存在");
//		}
//		return R.status(emailTemplateService.saveOrUpdate(template));
//	}
//
//	/**
//	 * 启用
//	 */
//	@PostMapping("/enable")
//	@ApiOperationSupport(order = 4)
//	@ApiOperation(value = "启用", notes = "传入ids")
//	public R enable(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//		return R.status(emailTemplateService.updateStatus(Func.toLongList(ids), CommonConstant.YES));
//	}
//
//	/**
//	 * 禁用
//	 */
//	@PostMapping("/disable")
//	@ApiOperationSupport(order = 5)
//	@ApiOperation(value = "启用", notes = "传入ids")
//	public R disable(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//		return R.status(emailTemplateService.updateStatus(Func.toLongList(ids), CommonConstant.NO));
//	}

}
