package org.springblade.modules.out_buy_low.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/2 17:06
 * @Description:
 */
@Data
@Api("统计接口")
public class OutBuyQprQualityVO {
	@ApiModelProperty("自撤回")
	private Integer selfBack;

	@ApiModelProperty("退回")
	private Integer back;

	@ApiModelProperty("进行中")
	private Integer process;

	@ApiModelProperty("已办结")
	private Integer finish;
}
