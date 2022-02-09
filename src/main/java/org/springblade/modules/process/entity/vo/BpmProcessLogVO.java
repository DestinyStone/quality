package org.springblade.modules.process.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 13:59
 * @Description: 操作日志类
 */
@Data
@ApiModel(value = "操作日志类", description = "操作日志类")
public class BpmProcessLogVO {

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty("操作用户")
	private Long operatorUser;

	@ApiModelProperty("操作用户名称")
	private String operatorUserName;

	@ApiModelProperty("操作部门")
	private Long operatorDept;

	@ApiModelProperty("操作部门路径")
	private String operatorDeptPath;

	@ApiModelProperty("操作角色")
	private String operatorRole;

	@ApiModelProperty("操作状态")
	private String operatorStatus;

	@ApiModelProperty("操作结果")
	private String operatorResult;

	@ApiModelProperty("操作时间")
	private Date operatorTime;

	@ApiModelProperty("开始时间")
	private Date startTime;

	@ApiModelProperty("结束时间")
	private Date endTime;

	@ApiModelProperty("业务id")
	private String busId;

	@ApiModelProperty("流程id")
	private Long bpmId;

	@ApiModelProperty("耗时")
	private String intervalTime;
}
