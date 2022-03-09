package org.springblade.modules.process_low.excel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/9 19:44
 * @Description:
 */
@Data
@Api("工序内不良 excel")
public class OutBuyQprExcel {
	@ApiModelProperty("不良编号")
	private String code;

	/**
	 * 不良分类 0外购件 1内制件 2其他
	 */
	@ApiModelProperty("不良分类")
	private String type;

	/**
	 * 不良等级 0R 1S 2A 3B 4C 5批量 6停线
	 */
	@ApiModelProperty("不良等级")
	private String level;

	/**
	 * 联络书发行日
	 */
	@ApiModelProperty("联络书发行日")
	private String releaseTime;

	/**
	 * 机型  0 TNGA2.0
	 */
	@ApiModelProperty("机型")
	private String apparatusType;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品名")
	private String name;

	@ApiModelProperty("发现时间")
	private String findTime;

	/**
	 * 发生地点 0TNGA#1 1TNGA#2 ....
	 */
	@ApiModelProperty("发生地点")
	private String triggerAddress;

	@ApiModelProperty("事件概要")
	private String eventRemark;

	@ApiModelProperty("拜托事项")
	private String pleaseContent;

	@ApiModelProperty("不良图片")
	private String imgReportList;

	@ApiModelProperty("发现数量")
	private Long findQuantity;

	@ApiModelProperty("调查回复日期")
	private String checkReplyTime;

	@ApiModelProperty("发生原因")
	private String analyseTriggerCause;

	@ApiModelProperty("流出原因")
	private String analyseOutCause;

	@ApiModelProperty("发生对策")
	private String analyseTriggerStrategy;

	@ApiModelProperty("流出对策")
	private String analyseOutStrategy;

	@ApiModelProperty("结案日期")
	private String completeTime;

	/**
	 * 流程状态 0待审批 1审批中 2已结案 3退回 4自撤回
	 */
	@ApiModelProperty("状态")
	private String bpmStatus;

	@ApiModelProperty("当前业务环节")
	private String bpmNode;

	@ApiModelProperty("提交人")
	private String createUserName;

	@ApiModelProperty("提交时间")
	private String createTime;

}
