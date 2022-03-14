package org.springblade.modules.work.entity.vo;

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
public class SettleLogVO {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("业务类型 0工序内不良 1外购件不良 2检查法 3DI数据")
	private Integer serviceType;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("状态 0已发布 1已完成 2已结案")
	private Integer status;

	@ApiModelProperty("归属部门")
	private Long belongToDept;

	@ApiModelProperty("属于用户")
	private Long belongToUser;
}
