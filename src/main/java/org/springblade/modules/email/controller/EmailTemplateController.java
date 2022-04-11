package org.springblade.modules.email.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.core.anon.Idempotence;
import org.springblade.common.enums.EmailType;
import org.springblade.common.utils.CommonUtil;
import org.springblade.common.utils.EmailUtils;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.email.bean.dto.EmailTemplateDTO;
import org.springblade.modules.email.bean.dto.EmailTemplateTestDTO;
import org.springblade.modules.email.bean.entity.EmailTemplate;
import org.springblade.modules.email.bean.entity.EmailTemplateParam;
import org.springblade.modules.email.bean.vo.EmailTemplateVO;
import org.springblade.modules.email.service.IEmailTemplateParamService;
import org.springblade.modules.email.service.IEmailTemplateService;
import org.springblade.modules.email.wrapper.EmailTemplateParamsWrapper;
import org.springblade.modules.email.wrapper.EmailTemplateWrapper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

	/**
	 * 测试
	 */
	@PostMapping("/test")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "测试")
	@Idempotence
	public R detail(@RequestParam("id") Long id, @RequestBody @Valid EmailTemplateTestDTO testDTO){
		EmailTemplate detail = emailTemplateService.getById(id);
		if (detail == null) {
			throw new ServiceException("邮件模板不存在");
		}
		testDTO.setContent(Base64.decodeStr(testDTO.getContent()));
		testDTO.setTo(testDTO.getTo() + "@qq.com");
		try {
			EmailUtils.send(detail.getTitle(), testDTO.getContent(), testDTO.getTo(), EmailType.QQ);
		} catch (MessagingException e) {
			e.printStackTrace();
			return R.status(false);
		}
		return R.status(true);
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入template")
	public R<EmailTemplateVO> detail(@RequestParam("id") Long id) {
		EmailTemplate detail = emailTemplateService.getById(id);
		if (detail == null) {
			throw new ServiceException("邮件模板不存在");
		}
		EmailTemplateVO result = EmailTemplateWrapper.build().entityVO(detail);
		List<EmailTemplateParam> templateParams = emailTemplateParamService.getParamsByTemplateId(detail.getId());
		if (templateParams != null) {
			result.setParams(EmailTemplateParamsWrapper.build().listVO(templateParams));
		}

		return R.data(result);
	}

	/**
	 * 分页 邮件模板
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入template")
	public R<IPage<EmailTemplateVO>> list(EmailTemplate template, Query query) {
		LambdaQueryWrapper<EmailTemplate> wrapper = Wrappers.<EmailTemplate>lambdaQuery();
		wrapper.select(EmailTemplate::getId, EmailTemplate::getTitle, EmailTemplate::getCode, EmailTemplate::getStatus);
		wrapper.eq(template.getStatus() != null, EmailTemplate::getStatus, template.getStatus())
			.like(!StrUtil.isEmpty(template.getTitle()), EmailTemplate::getTitle, template.getTitle());
		IPage<EmailTemplate> pages = emailTemplateService.page(Condition.getPage(query), wrapper);
		return R.data(EmailTemplateWrapper.build().pageVO(pages));
	}

	/**
	 * 更新
	 */
	@PostMapping("/update/{id}")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "更新")
	public R update(@PathVariable("id") Long id, @Valid @RequestBody EmailTemplateDTO template) {
		template.setContent(Base64.decodeStr(template.getContent()));
		EmailTemplate update = CommonUtil.copy(template, EmailTemplate.class);
		update.setId(id);
		emailTemplateService.updateById(update);

		// 删除原有参数
		LambdaQueryWrapper<EmailTemplateParam> paramWrapper = new LambdaQueryWrapper<>();
		paramWrapper.eq(EmailTemplateParam::getTemplateId, id);
		emailTemplateParamService.remove(paramWrapper);

		// 新增现有参数
		if (template.getParams() != null && !template.getParams().isEmpty()) {
			List<EmailTemplateParam> collect = template.getParams().stream().map(item -> {
				EmailTemplateParam param = CommonUtil.copy(item, EmailTemplateParam.class);
				param.setTemplateId(id);
				return param;
			}).collect(Collectors.toList());
			emailTemplateParamService.saveBatch(collect);
		}

		return R.status(true);
	}

	/**
	 * 新增
	 *
	 * @return
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "新增")
	public R save(@Valid @RequestBody EmailTemplateDTO template) {
		template.setContent(Base64.decodeStr(template.getContent()));
		EmailTemplate oldTemplate = emailTemplateService.getTemplateByCode(template.getCode());
		if (oldTemplate != null) {
			throw new ServiceException("邮件模板编码已存在");
		}
		EmailTemplate insert = CommonUtil.copy(template, EmailTemplate.class);
		emailTemplateService.save(insert);
		if (template.getParams() != null && !template.getParams().isEmpty()) {
			List<EmailTemplateParam> collect = template.getParams().stream().map(item -> {
				EmailTemplateParam param = CommonUtil.copy(item, EmailTemplateParam.class);
				param.setTemplateId(insert.getId());
				return param;
			}).collect(Collectors.toList());
			emailTemplateParamService.saveBatch(collect);
		}
		return R.status(emailTemplateService.saveOrUpdate(template));
	}

	/**
	 * 启用/禁用
	 */
	@GetMapping("/enable/{status}")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "启用", notes = "传入ids")
	public R enable(@ApiParam(value = "主键集合", required = true) @RequestParam String ids, @PathVariable("status") Integer status) {
		return R.status(emailTemplateService.updateStatus(Func.toLongList(ids), status));
	}

}
