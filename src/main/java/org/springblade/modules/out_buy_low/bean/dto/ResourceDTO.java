package org.springblade.modules.out_buy_low.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 19:29
 * @Description:
 */
@Data
public class ResourceDTO {

	@ApiModelProperty("来源id")
	@NotNull(message = "来源id不能为空")
	private Long resourceId;

	@ApiModelProperty("来源类型 0qpr 1low")
	@NotNull(message = "来源类型不能为空")
	private Integer resourceType;

	/**
	 * 累赘
	 */
	@Data
	public static class Encumbrance {

		@ApiModelProperty("来源id")
		private Long resourceId;

		@ApiModelProperty("来源类型 0qpr 1low")
		private Integer resourceType;

		@ApiModelProperty("品番号")
		private String designation;

		@ApiModelProperty("品名")
		private String name;

		@ApiModelProperty("供应商名称")
		private String dutyDept;

	}

}
