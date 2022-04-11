package org.springblade.modules.phone.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/4/9 10:15
 * @Description:
 */
@Data
@Api("邮箱模板测试")
public class PhoneTemplateParamsTestDTO {
	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "默认值")
	private String defaultValue;

	@ApiModelProperty(value = "新值")
	private String newValue;
}
