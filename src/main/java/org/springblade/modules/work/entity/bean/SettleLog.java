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
 * @Date: 2022/2/11 11:18
 * @Description: 已办结日志
 */
@Data
@Api("已办结日志")
@TableName("bus_settle_log")
public class SettleLog {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("业务类型")
	private Integer serviceType;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("状态")
	private Integer status;

	@ApiModelProperty("归属部门")
	private Integer belongToDept;

}
