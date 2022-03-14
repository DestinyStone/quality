package org.springblade.modules.out_buy_low.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/1 16:10
 * @Description: 外构件不良审批
 */
@Data
@Api("外构件不良审批")
public class OutBuyQprApproveVO {

	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("不良编号")
	private String code;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品名")
	private String name;

	@ApiModelProperty("发生地点 0TNGA#1 1TNGA#2 .....")
	private Long triggerAddress;

	@ApiModelProperty("不良数量")
	private Long findQuantity;

	@ApiModelProperty("发现时间")
	private Date findTime;

	@ApiModelProperty("不良等级 0R 1S 2A 3B 4C 5批量 6停线")
	private Long level;

	@ApiModelProperty("供应商名称")
	private String dutyDept;

	@ApiModelProperty("责任部门/厂家名称")
	private String dutyDeptName;

	@ApiModelProperty("时间概要")
	private String eventRemark;

	@ApiModelProperty("拜托事项")
	private String pleaseContent;

	@ApiModelProperty("不良图示/测试报告")
	private String imgReportIds;

	@ApiModelProperty("机型,逗号隔开 0TNGA2.0")
	private String apparatusType;

	@ApiModelProperty("处理 0返还 1保留 2废弃 3其他")
	private Long dispostType;

	@ApiModelProperty("处理")
	private String dispost;

	@ApiModelProperty("流程状态 0待审批 1审批中 2已审批 3已结案 4自撤回")
	private Integer bpmStatus;

	@ApiModelProperty("当前业务环节 0不良联络书发行 1不良调查 2不良横展 3调查结果确认")
	private Integer bpmNode;

	@ApiModelProperty("创建用户")
	private Long createUser;

	@ApiModelProperty("创建用户名称")
	private String createUserName;

	@ApiModelProperty("创建时间")
	private Date createTime;


	@ApiModelProperty("查询key 标题/不良内容/供应商名称")
	private String searchKey;

	@ApiModelProperty("标签标识 0待办 1已办 2本部门已超期")
	private Integer tagFlag;

	@ApiModelProperty("审批状态过滤 -1全部 0自撤回 1已驳回 2进行中 3已办结")
	private Integer bpmStatusFilter;

	@ApiModelProperty("工序内不良信息标识 0非工序内不良 1工序内不良审批 2工序内不良QPR录入")
	private Integer processLowFlag;

	@ApiModelProperty("工序内不良Id")
	private Long processLowId;

	@ApiModelProperty("任务主键")
	private Long bpmId;

	@ApiModelProperty("推进状态 0正常推进 1已超期 2已延期")
	private Integer bpmPushStatus;

	@ApiModelProperty("审批节点描述")
	private String bpmRemark;

	@ApiModelProperty("流程状态 0待审批 1审批中 2已结案 3退回 4自撤回")
	private Integer processBpmStatus;

	@ApiModelProperty("操作时间")
	private Date operatorTime;

	@ApiModelProperty("开始时间")
	private Date startTime;

	@ApiModelProperty("结束时间")
	private Date endTime;

	@ApiModelProperty("审批节点标识")
	private String bpmFlag;

	@ApiModelProperty("来源类型 0bus_out_buy_qpr 1bpm_process")
	private Integer resourceType;

	@ApiModelProperty("催办消息")
	private Integer urgeQuality;

	@ApiModelProperty("延迟说明")
	private String putOfRemark;

	private Long deptId;

	private Long roleId;

	private Long userId;
}
