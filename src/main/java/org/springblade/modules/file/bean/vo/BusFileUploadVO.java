package org.springblade.modules.file.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/9 17:58
 * @Description:
 */
@Data
public class BusFileUploadVO {

	@ApiModelProperty("访问路径")
	private String url;

	@ApiModelProperty("服务器文件路径")
	private String serverUrl;
}
