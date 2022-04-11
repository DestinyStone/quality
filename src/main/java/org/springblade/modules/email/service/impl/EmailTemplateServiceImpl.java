package org.springblade.modules.email.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.email.bean.dto.EmailTemplateDTO;
import org.springblade.modules.email.bean.entity.EmailTemplate;
import org.springblade.modules.email.bean.entity.EmailTemplateParam;
import org.springblade.modules.email.mapper.EmailTemplateMapper;
import org.springblade.modules.email.service.IEmailTemplateParamService;
import org.springblade.modules.email.service.IEmailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/15 14:16
 * @Description:
 */
@Service
public class EmailTemplateServiceImpl extends BaseServiceImpl<EmailTemplateMapper, EmailTemplate> implements IEmailTemplateService {

    @Autowired
    private IEmailTemplateParamService templateParamService;

    @Override
    public boolean updateStatus(List<Long> ids, int status) {
        return update(Wrappers.<EmailTemplate>lambdaUpdate().set(EmailTemplate::getStatus, status).in(EmailTemplate::getId, ids));
    }

    @Override
    public EmailTemplate getTemplateByCode(String code) {
        return getTemplateByCode(code, null);
    }

    @Override
    public EmailTemplate getTemplateByCode(String code, Long excludeId) {
        List<EmailTemplate> templates = list(Wrappers.<EmailTemplate>lambdaQuery().eq(EmailTemplate::getCode, code)
                .ne(excludeId != null, EmailTemplate::getId, excludeId));
        return templates.isEmpty() ? null : templates.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(EmailTemplateDTO template) {
//        if (template.getId() != null) {
//            templateParamService.deleteParamsByTemplateId(template.getId());
//        }
//        boolean success = saveOrUpdate(template);
//        if (!Func.isEmpty(template.getParams())) {
//			template.getParams().forEach(param -> {
//                param.setTemplateId(templateDTO.getId());
//            });
//            templateParamService.saveBatch(templateDTO.getParams());
//        }
//        return success;
		return true;
    }

    @Override
    public String buildContent(Long id, Map<String, Object> paramValues) {
        return buildContent(getById(id), paramValues);
    }

    @Override
    public String buildContent(String code, Map<String, Object> paramValues) {
        return buildContent(getTemplateByCode(code), paramValues);
    }

    @Override
    public String buildContent(EmailTemplate template, Map<String, Object> paramValues) {
        if (template == null) {
            throw new ServiceException("邮件模板不能为空");
        }
        List<EmailTemplateParam> params = templateParamService.getParamsByTemplateId(template.getId());
        Map<String, EmailTemplateParam> paramMap = params.stream().collect(Collectors.toMap(EmailTemplateParam::getName, param -> param));

        Map<String, Object> values = new HashMap<>(params.size());
        String content = template.getContent();

        for (String key : paramMap.keySet()) {
            EmailTemplateParam param = paramMap.get(key);
            // 如果传入的参数为空并且默认值不为空，则取参数默认值
            if (!Func.isEmpty(paramValues.get(key))) {
                values.put(key, paramValues.get(key));
                content = content.replaceAll(ReUtil.escape(String.format("#{%s}", key)), String.valueOf(values.get(key)));
            } else if (!Func.isEmpty(param.getDefaultValue())) {
                values.put(key, param.getDefaultValue());
                content = content.replaceAll(ReUtil.escape(String.format("#{%s}", key)), String.valueOf(values.get(key)));
            }
        }
        return content;
    }

	@Override
	public EmailTemplate getByCode(String code) {
		LambdaQueryWrapper<EmailTemplate> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(EmailTemplate::getCode, code);
		return getOne(wrapper);
	}
}
