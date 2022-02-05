package org.springblade.modules.process.entity.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/5 11:53
 * @Description:
 */
@Data
@TableName("bpm_process_urge")
@ApiModel(value = "流程催促", description = "流程催促")
public class BpmProcessUrge {

	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	@ApiModelProperty("流程id")
	@NotNull(message = "流程id不能为空")
	private Long bpmId;

	@ApiModelProperty("内容")
	private String content;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("创建用户")
	private Long createUser;

	@ApiModelProperty("创建用户名称")
	private String createUserName;

	@ApiModelProperty("创建部门")
	private Long createDept;

	@ApiModelProperty("创建部门名称")
	private String createDeptName;

	@ApiModelProperty("创建角色")
	private String createRole;
}
