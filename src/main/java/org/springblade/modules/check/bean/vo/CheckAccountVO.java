package org.springblade.modules.check.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/24 19:06
 * @Description: 检查法台账 视图类
 */
@Data
@Api("检查法台账 视图类")
public class CheckAccountVO {
	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("编码")
	private String code;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品名")
	private String name;

	@ApiModelProperty("供应商名称")
	private String dutyDept;

	@ApiModelProperty("审批状态")
	private Integer bpmStatus;

	@ApiModelProperty("供应商承认excel文件id")
	private Long providerExcelFileId;

	@ApiModelProperty("供应商承认excel文件名称")
	private String providerExcelFileName;

	@ApiModelProperty("供应商承认签字文件id")
	private Long providerSignatureId;

	@ApiModelProperty("供应商承认签字文件名称")
	private String providerSignatureName;

	@ApiModelProperty("旧 丰田商承认签字文件id")
	private Long oldToyotaExcelFileId;

	@ApiModelProperty("旧 丰田商承认签字文件名称")
	private String oldToyotaExcelFileName;

	@ApiModelProperty("丰田商承认签字文件id")
	private Long toyotaExcelFileId;

	@ApiModelProperty("丰田商承认签字文件名称")
	private String toyotaExcelFileName;

	@ApiModelProperty("查询key")
	private String searchKey;
}
