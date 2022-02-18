package org.springblade.modules.check.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 19:29
 * @Description:
 */
@Data
public class CheckResourceDTO {

	@ApiModelProperty("来源id")
	@NotNull(message = "来源id不能为空")
	private Long resourceId;

	@ApiModelProperty("来源类型 0qpr 1low")
	@NotNull(message = "来源类型不能为空")
	private Integer resourceType;
}
