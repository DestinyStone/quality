package org.springblade.modules.phone.bean.dto;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.common.constant.ALiConstant;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: DestinyStone
 * @Date: 2022/3/16 12:57
 * @Description:
 */
@Data
@Api("邮箱模板测试")
public class PhoneTemplateTestDTO {

	@ApiModelProperty(value = "内容")
	@NotBlank(message = "内容不能为空")
	private String content;

	@ApiModelProperty(value = "接受邮箱")
	@NotBlank(message = "接受邮箱不能为空")
	private String to;

	private List<PhoneTemplateParamsTestDTO> params;

	public Map<String, String> toParamsMap() {
		if (params == null || params.isEmpty()) {
			return new HashMap<>();
		}

		HashMap<String, String> map = new HashMap<>();
		for (PhoneTemplateParamsTestDTO param : params) {
			if (StrUtil.isBlank(param.getNewValue()) && StrUtil.isBlank( param.getDefaultValue())) {
				param.setNewValue(ALiConstant.TEST_DEFAULT);
			}
			map.put(param.getName(), StrUtil.isBlank(param.getNewValue()) ? param.getDefaultValue() : param.getNewValue());
		}
		return map;
	}
}
