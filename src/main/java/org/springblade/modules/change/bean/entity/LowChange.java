package org.springblade.modules.change.bean.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 19:21
 * @Description: 工变 实体
 */
@Data
@Api("工变 实体")
@TableName("bus_low_change")
public class LowChange {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("编号")
	private String code;

	@ApiModelProperty("机型 0 TNGA1.5 1TNGA2.0 2NR 3AZ")
	private Integer apparatusType;

	@ApiModelProperty("生产线 0TNGA#1 1TNGA#2 .... 5TNGA#5")
	private String productLine;

	@ApiModelProperty("工序号 0 TNGA1.5 1TNGA2.0 2NR 3AZ")
	private Integer processType;

	@ApiModelProperty("设备号类型 0 MK设备 1C设备 2刀具")
	private Integer utilType;

	@ApiModelProperty("设备号内容")
	private String utilContent;

	@ApiModelProperty("工变类型 0品质改善 1成本降低 2生产性提高 3其他")
	private String type;

	@ApiModelProperty("主要变化点 0生成工序的新设或改善 1更换材料 2加工")
	private String mainChangeType;

	@ApiModelProperty("背景")
	private String background;

	@ApiModelProperty("变更内容")
	private String changeContent;

	@ApiModelProperty("主要功能")
	private String mainFunction;

	@ApiModelProperty("主要不良")
	private String mainLow;

	@ApiModelProperty("相关附件")
	private String extendsFileIds;

	@ApiModelProperty("归口部门担当, 系长")
	private String belongUserIds;

	@ApiModelProperty("实施确认担当, 系长")
	private String executionUserIds;

	@ApiModelProperty("设备改造实施")
	private String executionUtilChangeJson;

	@ApiModelProperty("作业实施")
	private String executionWorkJson;

	@ApiModelProperty("品质实施")
	private String executionQualityJson;

	@ApiModelProperty("工艺实施")
	private String executionCraftJson;

	@ApiModelProperty("模具实施")
	private String executionModelJson;

	@ApiModelProperty("刀具实施")
	private String executionCutterJson;

	@ApiModelProperty("是否可切替 0不可切替 1可切替")
	private Integer isSwitch;

	@ApiModelProperty("不可切替原因")
	private String noSwitchCase;

	@ApiModelProperty("切替时间")
	private Date switchTime;

	@ApiModelProperty("切替意见")
	private String switchCase;

	@ApiModelProperty("设备改造是否确认 0否 1是")
	private Integer isUtilChangeConfirm;

	@ApiModelProperty("作业实施是否确认 0否 1是")
	private Integer isWorkChangeConfirm;

	@ApiModelProperty("品质实施是否确认 0否 1是")
	private Integer isQualityConfirm;

	@ApiModelProperty("工艺实施是否确认  0否 1是")
	private Integer isCraftConfirm;

	@ApiModelProperty("模具实施是否确认 0否 1是")
	private Integer isModelConfirm;

	@ApiModelProperty("刀具实施是否确认 0否 1是")
	private Integer isCutterConfirm;

	@ApiModelProperty("创建用户")
	private Long createUser;

	@ApiModelProperty("创建部门")
	private Long createDept;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("更新用户")
	private Long updateUser;

	@ApiModelProperty("更新时间")
	private Date updateTime;

	@ApiModelProperty("流程状态 0待审批 1审批中 2已结案 3退回 4自撤回")
	private Integer bpmStatus;

	@ApiModelProperty("当前业务环节 0申请部门审批 1归口部门审批 2实施确认 3实施结果录入 4实施结果审批 5切替判断 6完成确认 7完成审批")
	private Integer bpmNode;
}
