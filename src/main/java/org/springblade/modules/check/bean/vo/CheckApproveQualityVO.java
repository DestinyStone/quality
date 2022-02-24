package org.springblade.modules.check.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/1 12:34
 * @Description: 检查法审批统计
 */
@Data
@Api("检查法审批统计")
public class CheckApproveQualityVO {

	@ApiModelProperty("待办")
	private Integer await;

	@ApiModelProperty("已办")
	private Integer finish;

	@ApiModelProperty("过期")
	private Integer staleDated;
}
