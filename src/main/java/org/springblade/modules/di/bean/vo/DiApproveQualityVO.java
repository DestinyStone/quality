package org.springblade.modules.di.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 16:56
 * @Description:
 */
@Data
@Api("di 审批统计")
public class DiApproveQualityVO {
	@ApiModelProperty("待办")
	private Integer await;

	@ApiModelProperty("已办")
	private Integer finish;

	@ApiModelProperty("过期")
	private Integer staleDated;
}
