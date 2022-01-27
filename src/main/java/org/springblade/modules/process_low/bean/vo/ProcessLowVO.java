package org.springblade.modules.process_low.bean.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 10:54
 * @Description: 工序内不良
 */
@Data
@Api("工序内不良")
@TableName("bus_process_low")
public class ProcessLowVO {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键")
	private Long id;
	
	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("不良编号")
	private Long code;

	@ApiModelProperty("不良分类 0外购件 1内制件 2其他 ")
	private Integer type;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品名")
	private String name;

	@ApiModelProperty("发生地点 0TNGA#1 1TNGA#2 .....")
	private Integer triggerAddress;

	@ApiModelProperty("发生工序 0铸造钢体 1铸造缸盖 2缸盖加工 3缸体加工 4曲轴加工 5连杆加工 6凸轮轴加工")
	private Integer triggerProcess;

	@ApiModelProperty("发现数量")
	private Long findQuantity;

	@ApiModelProperty("发现时间")
	private Date findTime;

	@ApiModelProperty("不良等级 0R 1S 2A 3B 4C 5批量 6停线")
	private Integer level;

	@ApiModelProperty("不良内容")
	private String content;

	@ApiModelProperty("责任部门/厂家")
	private String dutyDept;

	@ApiModelProperty("拜托内容")
	private String pleaseContent;

	@ApiModelProperty("测试结果")
	private String testResult;

	@ApiModelProperty("关联部件确认")
	private String correlationConfirm;

	@ApiModelProperty("图片说明")
	private String imgRemark;

	@ApiModelProperty("机型 0TNGA2.0")
	private Integer apparatusType;

	@ApiModelProperty("不良测定报告文件ID")
	private Long testReportFileId;

	@ApiModelProperty("不良测定报告文件名称")
	private String testReportFileName;

	@ApiModelProperty("禁止流出描述")
	private String banOutflowRemark;

	@ApiModelProperty("追溯描述")
	private String formRemark;

	@ApiModelProperty("生产描述")
	private String productRemark;

	@ApiModelProperty("返修确认")
	private String rebackFixConfirm;

	@ApiModelProperty("不良排查及隔离文件ID")
	private Long separateFileId;

	@ApiModelProperty("不良排查及文件名称")
	private String separateFileName;

	@ApiModelProperty("变化点")
	private String changeRemark;

	@ApiModelProperty("是否存在通知文件")
	private Integer isBusinessFile;

	@ApiModelProperty("通知文件ID集合")
	private String busincessIdFiles;

	@ApiModelProperty("流程状态 0待审批 1审批中 2已结案 3退回 4自撤回")
	private Integer bpmStatus;

	@ApiModelProperty("流程节点 0不良联络书发行 1不良调查 2不良横展 3调查结果确认")
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
