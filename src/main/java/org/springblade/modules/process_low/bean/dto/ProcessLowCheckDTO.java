package org.springblade.modules.process_low.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/31 10:30
 * @Description:
 */
@Data
public class ProcessLowCheckDTO {

	@ApiModelProperty("不良分析调查原因")
	@NotBlank(message = "不良分析调查原因不能为空")
	private String analyseCause;

	@ApiModelProperty("不良分析调查图片")
	@NotNull(message = "不良分析调查图片不能为空")
	private Long analyseCauseImgFileId;

	@ApiModelProperty("调查原因类型 0异常处置 1变化点管理 2良品条件设定 3维持管理 4标准作业 5其他")
	@NotNull(message = "调查原因类型不能为空")
	private Integer analyseCauseType;

	@ApiModelProperty("调查发生原因")
	@NotBlank(message = "调查发生原因不能为空")
	private String analyseTriggerCause;

	@ApiModelProperty("调查发生对策")
	@NotBlank(message = "调查发生对策不能为空")
	private String analyseTriggerStrategy;

	@ApiModelProperty("调查流出原因")
	@NotBlank(message = "调查流出原因不能为空")
	private String analyseOutCause;

	@ApiModelProperty("调查流程对策")
	@NotBlank(message = "调查流程对策不能为空")
	private String analyseOutStrategy;

	@ApiModelProperty("是否上传标准文件")
	@NotNull(message = "是否上传标准文件不能为空")
	private Integer isUploadStandardFile;

	@ApiModelProperty("发生对策附件Id")
	private Long triggerStrategyFileId;

	@ApiModelProperty("发生对策附件名称")
	private String triggerStrategyFileName;

	@ApiModelProperty("流程对策文件Id")
	private Long processStrategyFileId;

	@ApiModelProperty("流程对策文件名称")
	private String processStrategyFileName;

	@ApiModelProperty("放行通知书附件Id")
	private Long passAdviceFileId;

	@ApiModelProperty("放行通知书附件名称")
	private String passAdviceFileName;

	@ApiModelProperty("标准文件附件Id")
	private Long standardFileId;

	@ApiModelProperty("标准文件附件名称")
	private String standardFileName;
}
