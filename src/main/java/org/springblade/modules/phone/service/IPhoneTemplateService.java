package org.springblade.modules.phone.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.phone.bean.dto.PhoneTemplateDTO;
import org.springblade.modules.phone.bean.entity.PhoneTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/15 14:16
 * @Description:
 */
public interface IPhoneTemplateService extends BaseService<PhoneTemplate> {

    /**
     * 更新状态
     *
     * @param ids    待更新的ID列表
     * @param status 状态
     * @return
     */
    boolean updateStatus(List<Long> ids, int status);

	/**
	 * 更新状态
	 *
	 * @param id   待更新的ID列表
	 * @param status 状态
	 * @return
	 */
	boolean updateStatus(Long id, int status);

    /**
     * 通过 code 查询邮件模板
     *
     * @param code 邮件编码
     * @return
     */
    PhoneTemplate getTemplateByCode(String code);

    /**
     * 通过 code 查询邮件模板
     *
     * @param code      邮件编码
     * @param excludeId 排除模板ID
     * @return
     */
    PhoneTemplate getTemplateByCode(String code, Long excludeId);

    /**
     * 保存后更新
     *
     * @param templateDTO
     * @return
     */
    boolean saveOrUpdate(PhoneTemplateDTO templateDTO);

    /**
     * 拼接邮件信息
     *
     * @param id          邮件模板ID
     * @param paramValues 邮件参数
     * @return
     */
    String buildContent(Long id, Map<String, Object> paramValues);

    /**
     * 拼接邮件信息
     *
     * @param code          邮件编码
     * @param paramValues 邮件参数
     * @return
     */
    String buildContent(String code, Map<String, Object> paramValues);

    /**
     * 拼接邮件信息
     *
     * @param template 邮件模板
     * @param values   邮件参数
     * @return
     */
    String buildContent(PhoneTemplate template, Map<String, Object> values);

}
