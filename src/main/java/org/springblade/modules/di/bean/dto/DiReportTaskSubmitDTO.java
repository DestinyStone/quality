package org.springblade.modules.di.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 18:24
 * @Description:
 */
@Data
public class DiReportTaskSubmitDTO {
	@ApiModelProperty("丰田承认文件Id")
	@NotNull(message = "丰田承认文件Id不能为空")
	private Long toyoDataFileId;

	@ApiModelProperty("丰田承认文件名称")
	@NotBlank(message = "丰田承认文件名称不能为空")
	private String toyoDataFileName;
}
