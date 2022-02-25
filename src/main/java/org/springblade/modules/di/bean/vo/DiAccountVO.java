package org.springblade.modules.di.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/25 11:25
 * @Description: di数据台账 视图类
 */
@Data
@Api("di数据台账 视图类")
public class DiAccountVO {
	private Long id;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品名")
	private String name;

	@ApiModelProperty("供应商名称")
	private String dutyDept;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("查询key")
	private String searchKey;
}
