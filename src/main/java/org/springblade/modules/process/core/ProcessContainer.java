package org.springblade.modules.process.core;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/29 00:38
 * @Description:
 */
@Data
public class ProcessContainer {

	@ApiModelProperty("业务Id")
	private String busId;

	@ApiModelProperty("流程描述")
	private String remark;

	@ApiModelProperty("排序")
	private Integer sort;

	@ApiModelProperty("允许的部门")
	private Long accessDept;

	@ApiModelProperty("状态")
	private Integer status;
}
