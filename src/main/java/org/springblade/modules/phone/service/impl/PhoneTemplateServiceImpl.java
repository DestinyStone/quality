package org.springblade.modules.phone.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.phone.bean.dto.PhoneTemplateDTO;
import org.springblade.modules.phone.bean.entity.PhoneTemplate;
import org.springblade.modules.phone.bean.entity.PhoneTemplateParam;
import org.springblade.modules.phone.mapper.PhoneTemplateMapper;
import org.springblade.modules.phone.service.IPhoneTemplateParamService;
import org.springblade.modules.phone.service.IPhoneTemplateService;
import org.springblade.modules.phone.utils.PhoneTemplateUtils;
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
public class PhoneTemplateServiceImpl extends BaseServiceImpl<PhoneTemplateMapper, PhoneTemplate> implements IPhoneTemplateService {

    @Autowired
    private IPhoneTemplateParamService templateParamService;

    @Override
    public boolean updateStatus(List<Long> ids, int status) {
		for (Long id : ids) {
			updateStatus(id, status);
		}
		return true;
    }

	@Override
	public boolean updateStatus(Long id, int status) {
		if (status == 1) {
			try {
				PhoneTemplateUtils.testEmail(id);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException("激活失败, 请确定线路是否打通");
			}
		}

		return update(Wrappers.<PhoneTemplate>lambdaUpdate().set(PhoneTemplate::getStatus, status).eq(PhoneTemplate::getId, id));
	}

	@Override
    public PhoneTemplate getTemplateByCode(String code) {
        return getTemplateByCode(code, null);
    }

    @Override
    public PhoneTemplate getTemplateByCode(String code, Long excludeId) {
        List<PhoneTemplate> templates = list(Wrappers.<PhoneTemplate>lambdaQuery().eq(PhoneTemplate::getCode, code)
                .ne(excludeId != null, PhoneTemplate::getId, excludeId));
        return templates.isEmpty() ? null : templates.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(PhoneTemplateDTO template) {
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
    public String buildContent(PhoneTemplate template, Map<String, Object> paramValues) {
        if (template == null) {
            throw new ServiceException("邮件模板不能为空");
        }
        List<PhoneTemplateParam> params = templateParamService.getParamsByTemplateId(template.getId());
        Map<String, PhoneTemplateParam> paramMap = params.stream().collect(Collectors.toMap(PhoneTemplateParam::getName, param -> param));

        Map<String, Object> values = new HashMap<>(params.size());
        String content = template.getContent();

        for (String key : paramMap.keySet()) {
            PhoneTemplateParam param = paramMap.get(key);
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
}
