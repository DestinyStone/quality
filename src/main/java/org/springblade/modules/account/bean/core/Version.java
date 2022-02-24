package org.springblade.modules.account.bean.core;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/24 20:51
 * @Description: 通用继承类
 */
@Data
@Api("通用继承类")
public class Version {

	@ApiModelProperty("版本号")
	private String version;

	@ApiModelProperty("创建时间")
	private Date createTime;
}
