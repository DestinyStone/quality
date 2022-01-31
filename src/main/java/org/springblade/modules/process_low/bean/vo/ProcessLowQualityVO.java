package org.springblade.modules.process_low.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/31 12:48
 * @Description: 工序内不良统计
 */
@Data
public class ProcessLowQualityVO {

	@ApiModelProperty("自撤回")
	private Integer selfBack;

	@ApiModelProperty("退回")
	private Integer back;

	@ApiModelProperty("进行中")
	private Integer process;

	@ApiModelProperty("已办结")
	private Integer finish;
}
