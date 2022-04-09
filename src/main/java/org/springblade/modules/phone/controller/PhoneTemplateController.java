package org.springblade.modules.phone.controller;

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
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.phone.bean.dto.PhoneTemplateDTO;
import org.springblade.modules.phone.bean.dto.PhoneTemplateTestDTO;
import org.springblade.modules.phone.bean.entity.PhoneTemplate;
import org.springblade.modules.phone.bean.entity.PhoneTemplateParam;
import org.springblade.modules.phone.bean.vo.PhoneTemplateVO;
import org.springblade.modules.phone.service.IPhoneTemplateParamService;
import org.springblade.modules.phone.service.IPhoneTemplateService;
import org.springblade.modules.phone.utils.PhoneTemplateUtils;
import org.springblade.modules.phone.wrapper.PhoneTemplateParamsWrapper;
import org.springblade.modules.phone.wrapper.PhoneTemplateWrapper;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping(RootMappingConstant.root + "/phone-template")
@Api(value = "手机短信模板", tags = "手机短信模板")
public class PhoneTemplateController {

	private IPhoneTemplateService templateService;

	private IPhoneTemplateParamService templateParamService;

	/**
	 * 激活通知
	 */
	@PostMapping("/advice")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "激活通知")
	public R advice(@RequestParam("id") Long id){
		// TODO
		return R.status(true);
	}

	/**
	 * 测试
	 */
	@PostMapping("/test")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "测试")
	@Idempotence
	public R detail(@RequestParam("id") Long id, @RequestBody @Valid PhoneTemplateTestDTO testDTO) throws Exception {
		PhoneTemplate detail = templateService.getById(id);
		if (detail == null) {
			throw new ServiceException("邮件模板不存在");
		}

		 PhoneTemplateUtils.sendEmail(detail.getCode(), testDTO.getTo(), testDTO.toParamsMap());
		return R.status(true);
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入template")
	public R<PhoneTemplateVO> detail(@RequestParam("id") Long id) {
		PhoneTemplate detail = templateService.getById(id);
		if (detail == null) {
			throw new ServiceException("邮件模板不存在");
		}
		PhoneTemplateVO result = PhoneTemplateWrapper.build().entityVO(detail);
		List<PhoneTemplateParam> templateParams = templateParamService.getParamsByTemplateId(detail.getId());
		if (templateParams != null) {
			result.setParams(PhoneTemplateParamsWrapper.build().listVO(templateParams));
		}

		return R.data(result);
	}

	/**
	 * 分页 邮件模板
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入template")
	public R<IPage<PhoneTemplateVO>> list(PhoneTemplate template, Query query) {
		LambdaQueryWrapper<PhoneTemplate> wrapper = Wrappers.<PhoneTemplate>lambdaQuery();
		wrapper.select(PhoneTemplate::getId, PhoneTemplate::getTitle, PhoneTemplate::getCode, PhoneTemplate::getStatus);
		wrapper.eq(template.getStatus() != null, PhoneTemplate::getStatus, template.getStatus())
			.like(!StrUtil.isEmpty(template.getTitle()), PhoneTemplate::getTitle, template.getTitle());
		IPage<PhoneTemplate> pages = templateService.page(Condition.getPage(query), wrapper);
		return R.data(PhoneTemplateWrapper.build().pageVO(pages));
	}

	/**
	 * 更新
	 */
	@PostMapping("/update/{id}")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "更新")
	public R update(@PathVariable("id") Long id, @Valid @RequestBody PhoneTemplateDTO template) {
		template.setContent(Base64.decodeStr(template.getContent()));
		PhoneTemplate update = CommonUtil.copy(template, PhoneTemplate.class);
		update.setId(id);
		update.setStatus(0);
		templateService.updateById(update);

		// 删除原有参数
		LambdaQueryWrapper<PhoneTemplateParam> paramWrapper = new LambdaQueryWrapper<>();
		paramWrapper.eq(PhoneTemplateParam::getTemplateId, id);
		templateParamService.remove(paramWrapper);

		// 新增现有参数
		if (template.getParams() != null && !template.getParams().isEmpty()) {
			List<PhoneTemplateParam> collect = template.getParams().stream().map(item -> {
				PhoneTemplateParam param = CommonUtil.copy(item, PhoneTemplateParam.class);
				param.setTemplateId(id);
				return param;
			}).collect(Collectors.toList());
			templateParamService.saveBatch(collect);
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
	public R save(@Valid @RequestBody PhoneTemplateDTO template) {
		template.setContent(Base64.decodeStr(template.getContent()));
		PhoneTemplate oldTemplate = templateService.getTemplateByCode(template.getCode());
		if (oldTemplate != null) {
			throw new ServiceException("邮件模板编码已存在");
		}
		PhoneTemplate insert = CommonUtil.copy(template, PhoneTemplate.class);
		insert.setStatus(0);
		templateService.save(insert);
		if (template.getParams() != null && !template.getParams().isEmpty()) {
			List<PhoneTemplateParam> collect = template.getParams().stream().map(item -> {
				PhoneTemplateParam param = CommonUtil.copy(item, PhoneTemplateParam.class);
				param.setTemplateId(insert.getId());
				return param;
			}).collect(Collectors.toList());
			templateParamService.saveBatch(collect);
		}
		return R.status(templateService.saveOrUpdate(template));
	}

	/**
	 * 启用/禁用
	 */
	@GetMapping("/enable/{status}")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "启用", notes = "传入ids")
	public R enable(@ApiParam(value = "主键集合", required = true) @RequestParam String ids, @PathVariable("status") Integer status) {
		return R.status(templateService.updateStatus(Func.toLongList(ids), status));
	}

}
