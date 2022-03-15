package org.springblade.modules.email.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.modules.email.bean.entity.EmailTemplateParam;

import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/15 14:16
 * @Description:
 */
public interface IEmailTemplateParamService extends IService<EmailTemplateParam> {

    /**
     * 通过模板 ID 查询所有邮件模板参数
     *
     * @param templateId 模板ID
     * @return
     */
    List<EmailTemplateParam> getParamsByTemplateId(Long templateId);

    /**
     * 通过模板 ID 删除所有邮件模板参数
     *
     * @param templateId 模板ID
     * @return
     */
    int deleteParamsByTemplateId(Long templateId);

}
