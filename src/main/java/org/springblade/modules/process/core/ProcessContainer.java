package org.springblade.modules.process.core;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/29 00:38
 * @Description:
 */
@Data
public class ProcessContainer {

	@ApiModelProperty("流程描述")
	private String remark;

	@ApiModelProperty("排序")
	private String sort;

	@ApiModelProperty("允许的部门")
	private Long accessDept;

	@ApiModelProperty("开始时间")
	private Date startTime;

	@ApiModelProperty("结束时间")
	private Date endTime;

	@ApiModelProperty("状态")
	private Integer status;
}
