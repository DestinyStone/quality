package org.springblade.modules.change.bean.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 19:21
 * @Description: 工变 实体
 */
@Data
@Api("工变 实体")
@TableName("bus_low_change")
public class LowChangeDTO {

	@ApiModelProperty("机型 0 TNGA1.5 1TNGA2.0 2NR 3AZ")
	@NotNull(message = "机型不能为空")
	private Integer apparatusType;

	@ApiModelProperty("生产线 0TNGA#1 1TNGA#2 .... 5TNGA#5")
	@NotNull(message = "生产线不能为空")
	private String productLine;

	@ApiModelProperty("工序号 0 TNGA1.5 1TNGA2.0 2NR 3AZ")
	@NotNull(message = "工序号不能为空")
	private Integer processType;

	@ApiModelProperty("设备号类型 0 MK设备 1C设备 2刀具")
	@NotNull(message = "设备号类型不能为空")
	private Integer utilType;

	@ApiModelProperty("设备号内容")
	@NotNull(message = "设备号内容不能为空")
	private String utilContent;

	@ApiModelProperty("工变类型 0品质改善 1成本降低 2生产性提高 3其他")
	@NotNull(message = "工变类型不能为空")
	private String type;

	@ApiModelProperty("主要变化点 0生成工序的新设或改善 1更换材料 2加工")
	@NotNull(message = "主要变化点不能为空")
	private String mainChangeType;

	@ApiModelProperty("背景")
	@NotNull(message = "背景不能为空")
	private String background;

	@ApiModelProperty("变更内容")
	@NotNull(message = "变更内容不能为空")
	private String changeContent;

	@ApiModelProperty("主要功能")
	private String mainFunction;

	@ApiModelProperty("主要不良")
	private String mainLow;

	@ApiModelProperty("相关附件")
	private String extendsFileIds;

	@ApiModelProperty("归口部门担当, 系长")
	@NotNull(message = "归口部门担当, 系长不能为空")
	private String belongUserIds;

	@ApiModelProperty("实施确认担当, 系长")
	@NotNull(message = "实施确认担当, 系长不能为空")
	private String executionUserIds;
}
