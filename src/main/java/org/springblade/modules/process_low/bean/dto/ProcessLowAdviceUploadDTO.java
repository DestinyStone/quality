package org.springblade.modules.process_low.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/9 14:03
 * @Description:
 */
@Data
@Api("通知书文件上传")
public class ProcessLowAdviceUploadDTO {

	@ApiModelProperty("业务通知书文件ID")
	private Long businessAdviceFileId;

	@ApiModelProperty("业务通知书文件名称")
	private String businessAdviceFileName;

	@ApiModelProperty("放行通知书文件ID")
	private Long passAdviceFileId;

	@ApiModelProperty("放行通知书文件名称")
	private String passAdviceFileName;
}
