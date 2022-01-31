package org.springblade.modules.process_low.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 10:54
 * @Description: 工序内不良
 */
@Data
@Api("工序内不良")
public class ProcessLowDTO {

	@ApiModelProperty("标题")
	@NotBlank(message = "标题不能为空")
	private String title;

	@ApiModelProperty("不良分类 0外购件 1内制件 2其他 ")
	@NotNull(message = "不良分类不能为空")
	private Integer type;

	@ApiModelProperty("品番号")
	@NotBlank(message = "品番号不能为空")
	private String designation;

	@ApiModelProperty("品名")
	@NotBlank(message = "品名不能为空")
	private String name;

	@ApiModelProperty("发生地点 0TNGA#1 1TNGA#2 .....")
	@NotNull(message = "发生地点不能为空")
	private Integer triggerAddress;

	@ApiModelProperty("发生工序 0铸造钢体 1铸造缸盖 2缸盖加工 3缸体加工 4曲轴加工 5连杆加工 6凸轮轴加工")
	@NotNull(message = "发生工序不能为空")
	private Integer triggerProcess;

	@ApiModelProperty("发现数量")
	@NotNull(message = "发现数量不能为空")
	private Long findQuantity;

	@ApiModelProperty("发现时间")
	@NotNull(message = "发现时间不能为空")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	private Date findTime;

	@ApiModelProperty("不良等级 0R 1S 2A 3B 4C 5批量 6停线")
	@NotNull(message = "不良等级不能为空")
	private Integer level;

	@ApiModelProperty("不良内容")
	@NotBlank(message = "不良内容不能为空")
	private String content;

	@ApiModelProperty("责任部门/厂家")
	@NotBlank(message = "责任部门/厂家不能为空")
	private String dutyDept;

	@ApiModelProperty("拜托内容")
	@NotBlank(message = "拜托内容不能为空")
	private String pleaseContent;

	@ApiModelProperty("测试结果")
	@NotBlank(message = "测试结果不能为空")
	private String testResult;

	@ApiModelProperty("关联部件确认")
	@NotBlank(message = "关联部件确认不能为空")
	private String correlationConfirm;

	@ApiModelProperty("不良图示/测试报告 Ids")
	@NotBlank(message = "不良图示/测试报告不能为空")
	private String imgReportIds;

	@ApiModelProperty("图片说明")
	private String imgRemark;

	@ApiModelProperty("机型 0TNGA2.0")
	@NotNull(message = "机型不能为空")
	private Integer apparatusType;

	@ApiModelProperty("不良测定报告文件ID")
	private Long testReportFileId;

	@ApiModelProperty("不良测定报告文件名称")
	private String testReportFileName;

	@ApiModelProperty("禁止流出描述")
	@NotBlank(message = "流出防止不能为空")
	private String banOutflowRemark;

	@ApiModelProperty("不良追溯描述")
	@NotBlank(message = "不良追溯不能为空")
	private String formRemark;

	@ApiModelProperty("生产对应描述")
	@NotBlank(message = "生产对应不能为空")
	private String productRemark;

	@ApiModelProperty("返修确认")
	private String rebackFixConfirm;

	@ApiModelProperty("不良排查及隔离文件ID")
	@NotNull(message = "不良排查及隔离不能为空")
	private Long separateFileId;

	@ApiModelProperty("不良排查及文件名称")
	private String separateFileName;

	@ApiModelProperty("不良排查及隔离附件文件ID")
	private Long separateDependFileId;

	@ApiModelProperty("不良排查及文件附件名称")
	private String separateDependFileName;

	@ApiModelProperty("变化点")
	@NotBlank(message = "变化点不能为空")
	private String changeRemark;

	@ApiModelProperty("是否存在通知文件")
	@NotNull(message = "是否存在通知文件不能为空")
	private Integer isBusinessFile;

	@ApiModelProperty("通知文件ID集合")
	private String busincessIdFiles;

	@ApiModelProperty("流程状态 0待审批 1审批中 2已结案 3退回 4自撤回")
	private Integer bpmStatus;

	@ApiModelProperty("流程节点 0不良联络书发行 1不良调查 2不良横展 3调查结果确认")
	private Integer bpmNode;

	@ApiModelProperty("创建用户")
	private String createUser;

	@ApiModelProperty("创建时间")
	private String createTime;

	@ApiModelProperty("创建部门")
	private String createDept;

	@ApiModelProperty("更新用户")
	private String updateUser;

	@ApiModelProperty("更新时间")
	private String updateTime;

	@ApiModelProperty("状态")
	private String status;
}
