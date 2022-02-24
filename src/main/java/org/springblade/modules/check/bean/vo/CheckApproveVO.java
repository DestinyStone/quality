package org.springblade.modules.check.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/23 18:31
 * @Description:
 */
@Data
@Api("检查法审批视图类")
public class CheckApproveVO {

	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("流程Id")
	private Long bpmId;

	@ApiModelProperty("业务类型 0新增检查法 1修改检查法")
	private Integer bpmNode;

	@ApiModelProperty("编码")
	private String code;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品名")
	private String name;

	@ApiModelProperty("供应商名称")
	private String dutyDept;

	@ApiModelProperty("丰田商承认签字文件id")
	private Long toyotaExcelFileId;

	@ApiModelProperty("丰田商承认签字文件名称")
	private String toyotaExcelFileName;

	@ApiModelProperty("推进状态 0正常推进 1已超期 2已延期")
	private Integer bpmPushStatus;

	@ApiModelProperty("业务状态 0待审批 1审批中 2已结案 3退回 4自撤回")
	private Integer bpmStatus;

	@ApiModelProperty("流程状态")
	private Integer processBpmStatus;

	@ApiModelProperty("开始时间")
	private Date startTime;

	@ApiModelProperty("结束时间")
	private Date endTime;

	@ApiModelProperty("审批标识")
	private String bpmFlag;

	@ApiModelProperty("催办消息")
	private Integer urgeQuality;

	@ApiModelProperty("查询标识 0待办 1已办 2本部门已超期")
	private Integer tagFlag;

	@ApiModelProperty("查询key")
	private String searchKey;
}
