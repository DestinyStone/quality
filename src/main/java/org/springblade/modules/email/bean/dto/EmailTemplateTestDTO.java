package org.springblade.modules.email.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: DestinyStone
 * @Date: 2022/3/16 12:57
 * @Description:
 */
@Data
@Api("邮箱模板测试")
public class EmailTemplateTestDTO {

	@ApiModelProperty(value = "内容")
	@NotBlank(message = "内容不能为空")
	private String content;

	@ApiModelProperty(value = "接受邮箱")
	@NotBlank(message = "接受邮箱不能为空")
	private String to;
}
