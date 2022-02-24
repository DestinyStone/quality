package org.springblade.modules.account.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/24 18:27
 * @Description: 台账 实体
 */
@Data
@TableName("bus_account")
@Api("台账 实体")
public class Account {

	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("标识")
	private String flag;

	@ApiModelProperty("业务Id")
	private Long busId;

	@ApiModelProperty("内容")
	private String content;

	@ApiModelProperty("版本")
	private String version;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("创建用户")
	private Long createUser;

	@ApiModelProperty("创建部门")
	private Long createDept;

	@ApiModelProperty("是否是最新版本")
	private Integer isNewVersion;
}
