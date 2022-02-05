package org.springblade.modules.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 11:53
 * @Description:
 */
@Data
@ApiModel(value = "流程催促", description = "流程催促")
public class BpmProcessUrgeDTO {

	@ApiModelProperty("流程id")
	@NotNull(message = "流程id不能为空")
	private Long bpmId;

	@ApiModelProperty("催办内容")
	@NotBlank(message = "催办内容不能为空")
	private String content;
}
