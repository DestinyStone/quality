package org.springblade.modules.out_buy_low.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 10:54
 * @Description: 外构件不良QPR
 */

@Data
@Api("外构件不良QPR")
@TableName("bus_out_buy_qpr")
public class OutBuyQpr {
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("不良编号")
	private String code;

	@ApiModelProperty("不良分类")
	private Integer type;

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

	@ApiModelProperty("事件概要")
	private String eventRemark;

	@ApiModelProperty("拜托事项")
	private String pleaseContent;

	@ApiModelProperty("不良图示/测试报告")
	private String imgReportIds;

	@ApiModelProperty("机型,逗号隔开 0TNGA2.0")
	private Integer apparatusType;

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

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("创建部门")
	private Long createDept;

	@ApiModelProperty("更新用户")
	private Long updateUser;

	@ApiModelProperty("更新时间")
	private Date updateTime;

	@ApiModelProperty("状态")
	private Long status;

	@ApiModelProperty("工序内不良信息标识 0非工序内不良 1工序内不良审批 2工序内不良QPR录入")
	private Integer processLowFlag;

	@ApiModelProperty("工序内不良Id")
	private Long processLowId;

	@ApiModelProperty("调查发生原因")
	private String analyseTriggerCause;

	@ApiModelProperty("调查流程原因")
	private String analyseOutCause;

	@ApiModelProperty("调查发生对策")
	private String analyseTriggerStrategy;

	@ApiModelProperty("调查流程对策")
	private String analyseOutStrategy;

	@ApiModelProperty("其他")
	private String analyseOther;

	@ApiModelProperty("相关附件Ids")
	private String analyseExtendsFileIds;

	@ApiModelProperty("属性 0单发 1散发 2批量")
	private Integer fillPropertiesType;

	@ApiModelProperty("不良类型 0漏工序 1欠品误品 2异音 3异物 4外观 5测漏NG 6尺寸 7其他")
	private Integer fillType;

	@ApiModelProperty("流出原因分类 0检出力低 2防错失效 3标准作业不足 4异常处置不当 5其他")
	private Integer fillOutCauseType;

	@ApiModelProperty("发生原因分类 0夹装异常 1加功条件管理不足 2标准作业不足 3异常处置不当 4变化点管理不足 5其他")
	private Integer fillTriggerCauseType;

	@ApiModelProperty("判断结果 0供应商责任 1责任不明结案 2生管责任 3其他直接结案 4是否其他结案")
	private Integer fillJudgeResult;

	@ApiModelProperty("描述")
	private String fillRemark;

	@ApiModelProperty("是否允许新增检查法")
	private Integer isAccessCheck;

	@ApiModelProperty("调查结果日期")
	private Date checkReplyTime;

	@ApiModelProperty("结案日期")
	private Date completeTime;
}
