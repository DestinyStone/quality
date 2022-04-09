package org.springblade.modules.phone.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.phone.bean.entity.PhoneTemplateParam;
import org.springblade.modules.phone.mapper.PhoneTemplateParamMapper;
import org.springblade.modules.phone.service.IPhoneTemplateParamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/15 14:16
 * @Description:
 */
@Service
public class PhoneTemplateParamServiceImpl extends ServiceImpl<PhoneTemplateParamMapper, PhoneTemplateParam> implements IPhoneTemplateParamService {

    @Override
    public List<PhoneTemplateParam> getParamsByTemplateId(Long templateId) {
        return list(Wrappers.<PhoneTemplateParam>lambdaQuery().eq(PhoneTemplateParam::getTemplateId, templateId));
    }

    @Override
    public int deleteParamsByTemplateId(Long templateId) {
        return baseMapper.deleteParamsByTemplateId(templateId);
    }
}
