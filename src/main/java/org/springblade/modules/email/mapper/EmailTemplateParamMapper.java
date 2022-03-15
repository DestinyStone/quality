package org.springblade.modules.email.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.modules.email.bean.entity.EmailTemplateParam;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/15 14:16
 * @Description:
 */
public interface EmailTemplateParamMapper extends BaseMapper<EmailTemplateParam> {

    /**
     * 通过模板 ID 删除所有邮件模板参数
     *
     * @param templateId 模板ID
     * @return
     */
    int deleteParamsByTemplateId(@Param("templateId") Long templateId);

}
