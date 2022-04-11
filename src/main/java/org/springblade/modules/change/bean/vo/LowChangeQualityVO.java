package org.springblade.modules.change.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/24 13:18
 * @Description: 检查法统计
 */
@Data
public class LowChangeQualityVO {
	@ApiModelProperty("自撤回")
	private Integer selfBack;

	@ApiModelProperty("退回")
	private Integer back;

	@ApiModelProperty("进行中")
	private Integer process;

	@ApiModelProperty("已办结")
	private Integer finish;
}
