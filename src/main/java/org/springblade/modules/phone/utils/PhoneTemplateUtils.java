package org.springblade.modules.phone.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springblade.common.constant.ALiConstant;
import org.springblade.common.utils.PhoneUtils;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.phone.bean.entity.PhoneTemplate;
import org.springblade.modules.phone.bean.entity.PhoneTemplateParam;
import org.springblade.modules.phone.service.IPhoneTemplateParamService;
import org.springblade.modules.phone.service.IPhoneTemplateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xiaoxia
 * @Date: 2022/4/8 11:05
 * @Description:
 */
public class PhoneTemplateUtils {

	public static IPhoneTemplateService templateService;

	public static IPhoneTemplateParamService paramService;

	static {
		templateService = SpringUtil.getBean(IPhoneTemplateService.class);
		paramService = SpringUtil.getBean(IPhoneTemplateParamService.class);
	}

	public static void sendEmail(String code, String to) throws Exception {
		sendEmail(code, to, new HashMap<>());
	}

	public static void testEmail(Long id) throws Exception {
		PhoneTemplate phoneTemplate = templateService.getById(id);
		testEmail(phoneTemplate.getCode());
	}

	public static void testEmail(String code) throws Exception {
		PhoneTemplate phoneTemplate = getTemplateAndValid(code);
		LambdaQueryWrapper<PhoneTemplateParam> paramsWrapper = new LambdaQueryWrapper<>();
		paramsWrapper.eq(PhoneTemplateParam::getTemplateId, phoneTemplate.getId());
		List<PhoneTemplateParam> paramList = paramService.list(paramsWrapper);
		HashMap<String, String> map = new HashMap<>();

		if (!paramList.isEmpty()) {
			for (PhoneTemplateParam param : paramList) {
				map.put(param.getName(), StrUtil.isBlank(param.getDefaultValue()) ? ALiConstant.TEST_DEFAULT : param.getDefaultValue());
			}
		}

		PhoneUtils.sendEmail(phoneTemplate.getConcatCode(), ALiConstant.TEST_PHONE, map);
	}

	public static void sendEmail(String code, String to, Map<String, String> map) throws Exception {
		PhoneTemplate phoneTemplate = getTemplateAndValid(code);

		if (phoneTemplate.getStatus() != 1) {
			throw new ServiceException("邮件模板未激活");
		}

		PhoneUtils.sendEmail(phoneTemplate.getConcatCode(), to, map);
	}

	public static void sendEmailSync(String code, String to, Map<String, String> map)  {
		new Thread(() -> {
			PhoneTemplate phoneTemplate = getTemplateAndValid(code);

			if (phoneTemplate.getStatus() != 1) {
				throw new ServiceException("邮件模板未激活");
			}
			try {
				PhoneUtils.sendEmail(phoneTemplate.getConcatCode(), to, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	private static PhoneTemplate getTemplateAndValid(String code) {
		LambdaQueryWrapper<PhoneTemplate> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(PhoneTemplate::getCode, code);
		PhoneTemplate phoneTemplate = templateService.getOne(wrapper);
		if (phoneTemplate == null) {
			throw new ServiceException("短信模板不存在");
		}

		if (phoneTemplate.getConcatCode() == null) {
			throw new ServiceException("缺少重要参数");
		}
		return phoneTemplate;
	}
}
