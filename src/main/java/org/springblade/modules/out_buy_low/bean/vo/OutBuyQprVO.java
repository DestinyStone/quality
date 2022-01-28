package org.springblade.modules.out_buy_low.bean.vo;

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
public class OutBuyQprVO {
	@TableId(value = "id", type = IdType.ASSIGN_ID)
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

	@ApiModelProperty("时间概要")
	private String eventRemark;

	@ApiModelProperty("拜托事项")
	private String pleaseContent;

	@ApiModelProperty("不良图示/测试报告")
	private String imgReportIds;

	@ApiModelProperty("机型,逗号隔开 0TNGA2.0")
	private String apparatusTypes;

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

	@ApiModelProperty("审批状态过滤 -1全部 0自撤回 1已驳回 2进行中 3已办结")
	private Integer bpmStatusFilter;
}
