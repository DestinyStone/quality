package org.springblade.modules.work.entity.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("bus_message")
public class Message {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
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

	@ApiModelProperty("属于部门")
	private Long belongToDept;
}
