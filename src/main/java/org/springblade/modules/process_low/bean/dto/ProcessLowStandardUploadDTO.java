package org.springblade.modules.process_low.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/9 14:16
 * @Description:
 */
@Data
@Api("标准类文件上传")
public class ProcessLowStandardUploadDTO {
	@ApiModelProperty("标准文件附件Id")
	private Long standardFileId;

	@ApiModelProperty("标准文件附件名称")
	private String standardFileName;
}
