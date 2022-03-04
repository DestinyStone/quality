package org.springblade.modules.di.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 15:02
 * @Description:
 */
@Data
@Api("di上报提交 DTO")
public class DiReportSubmitDTO {

	@ApiModelProperty("供应商Excel文件Id")
	private Long dataExcelFileId;

	@ApiModelProperty("供应商Excel文件名称")
	private String dataExcelFileName;

	@ApiModelProperty("供应商签名文件Id")
	private Long dataSignateFileId;

	@ApiModelProperty("供应商签名文件名称")
	private String dataSignateFileName;
}
