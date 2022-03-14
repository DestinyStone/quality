package org.springblade.modules.work.entity.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 9:29
 * @Description:
 */
@Data
@Api("站内消息")
public class MessageVO {

	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("路由类型")
	private Integer routerType;

	@ApiModelProperty("类型 0 站内消息")
	private Integer type;

	@ApiModelProperty("归属部门")
	private Long belongToDept;

	@ApiModelProperty("归属部门名称")
	private Long belongToDeptName;

	@ApiModelProperty("属于用户")
	private Long belongToUser;

	@ApiModelProperty("归属用户名称")
	private Long belongToUserName;
}
