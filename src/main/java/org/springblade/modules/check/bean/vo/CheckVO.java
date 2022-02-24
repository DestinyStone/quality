package org.springblade.modules.check.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/23 16:00
 * @Description:
 */
@Data
@Api("检查法 视图类")
public class CheckVO {

	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("编码")
	private String code;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品番名")
	private String name;

	@ApiModelProperty("供应商名称")
	private String dutyDept;

	@ApiModelProperty("0 新增检查法 1修改检查法")
	private Integer bpmNode;

	@ApiModelProperty("流程状态")
	private Integer bpmStatus;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("查询key 标题/不良内容/供应商名称")
	private String searchKey;

	@ApiModelProperty("审批状态过滤 -1全部 0自撤回 1已驳回 2进行中 3已办结")
	private Integer bpmStatusFilter;
}
