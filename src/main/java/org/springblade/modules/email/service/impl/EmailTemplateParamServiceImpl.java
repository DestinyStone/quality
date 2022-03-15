package org.springblade.modules.email.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.email.bean.entity.EmailTemplateParam;
import org.springblade.modules.email.mapper.EmailTemplateParamMapper;
import org.springblade.modules.email.service.IEmailTemplateParamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/15 14:16
 * @Description:
 */
@Service
public class EmailTemplateParamServiceImpl extends ServiceImpl<EmailTemplateParamMapper, EmailTemplateParam> implements IEmailTemplateParamService {

    @Override
    public List<EmailTemplateParam> getParamsByTemplateId(Long templateId) {
        return list(Wrappers.<EmailTemplateParam>lambdaQuery().eq(EmailTemplateParam::getTemplateId, templateId));
    }

    @Override
    public int deleteParamsByTemplateId(Long templateId) {
        return baseMapper.deleteParamsByTemplateId(templateId);
    }
}
