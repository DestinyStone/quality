package org.springblade.modules.email.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/16 9:28
 * @Description: 邮件参数
 */
@Data
@Api("邮件参数")
public class EmailTemplateParamsDTO {
	/**
	 * 中文名称
	 */
	@ApiModelProperty(value = "中文名称")
	@NotBlank(message = "中文名称")
	private String label;

	/**
	 * 英文名称
	 */
	@ApiModelProperty(value = "英文名称")
	@NotBlank(message = "英文名称")
	private String name;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 默认值
	 */
	@ApiModelProperty(value = "默认值")
	private String defaultValue;
}
