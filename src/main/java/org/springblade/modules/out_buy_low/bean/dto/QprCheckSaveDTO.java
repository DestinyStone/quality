package org.springblade.modules.out_buy_low.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/2 18:34
 * @Description:
 */
@Data
public class QprCheckSaveDTO {

	@ApiModelProperty("调查发生原因")
	@NotBlank(message = "发生原因不能为空")
	private String analyseTriggerCause;

	@ApiModelProperty("调查流程原因")
	@NotBlank(message = "流程原因不能为空")
	private String analyseOutCause;

	@ApiModelProperty("调查发生对策")
	@NotBlank(message = "发生对策不能为空")
	private String analyseTriggerStrategy;

	@ApiModelProperty("调查流程对策")
	@NotBlank(message = "流程对策不能为空")
	private String analyseOutStrategy;

	@ApiModelProperty("其他")
	private String analyseOther;

	@ApiModelProperty("相关附件Ids")
	private String analyseExtendsFileIds;
}
