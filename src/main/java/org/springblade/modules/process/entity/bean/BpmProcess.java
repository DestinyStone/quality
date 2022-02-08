package org.springblade.modules.process.entity.bean;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 11:00
 * @Description: 流程实体
 */
@Data
@TableName("bpm_process")
@ApiModel(value = "流程实体", description = "流程实体")
public class BpmProcess {

	@ApiModelProperty(value = "主键")
	@TableId(value = "bpm_id", type = IdType.ASSIGN_ID)
	private Long bpmId;

	@ApiModelProperty("流程描述")
	private String bpmRemark;

	@ApiModelProperty("状态 0锁定 1未激活 2已激活 3已完成 4已退回")
	private Integer bpmStatus;

	@ApiModelProperty("推进状态 0正常推进 1已超期 2已延期")
	private Integer bpmPushStatus;

	@ApiModelProperty("排序")
	private Integer bpmSort;

	@ApiModelProperty("标识")
	private String bpmFlag;

	@ApiModelProperty("业务Id")
	private String busId;

	@ApiModelProperty("捆绑id")
	private String bpmBingId;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("最晚操作时间")
	private Date endTime;

	@ApiModelProperty("操作时间")
	private Date operatorTime;

	@ApiModelProperty("操作用户")
	private Long operatorUser;

	@ApiModelProperty("操作用户名称")
	private String operatorUserName;

	@ApiModelProperty("操作部门")
	private Long operatorDept;

	@ApiModelProperty("操作部门路径")
	private String operatorDeptPath;

	@ApiModelProperty("操作角色")
	private String operatorRole;

	@ApiModelProperty("操作角色名称")
	private String operatorRoleName;

	@ApiModelProperty("允许操作部门")
	private Long accessDept;

	@ApiModelProperty("退回原因")
	private String backCause;

	@ApiModelProperty("延迟说明")
	private String putOfRemark;

	@ApiModelProperty("是否是废除的 0否 1是")
	private Integer isCastoff;
}
