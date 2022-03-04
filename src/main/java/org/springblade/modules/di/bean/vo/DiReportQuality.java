package org.springblade.modules.di.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 15:51
 * @Description:
 */
@Data
@Api("已上报列表统计")
public class DiReportQuality {

	@ApiModelProperty("进行中")
	private Long await;

	@ApiModelProperty("退回")
	private Long back;

	@ApiModelProperty("已完成")
	private Long finish;

	public static DiReportQuality getInit() {
		DiReportQuality entity = new DiReportQuality();
		entity.setAwait(0L);
		entity.setBack(0L);
		entity.setFinish(0L);
		return entity;
	}
}
