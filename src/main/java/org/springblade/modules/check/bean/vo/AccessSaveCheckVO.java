package org.springblade.modules.check.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 18:49
 * @Description: 允许新增检查法 视图类
 */
@Data
@Api("允许新增检查法 视图类")
public class AccessSaveCheckVO {

	@ApiModelProperty("来源主键")
	private Long resourceId;

	@ApiModelProperty("来源类型 0qpr 1low")
	private Integer resourceType;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品名")
	private String name;

	@ApiModelProperty("供应商名称")
	private String dutyDept;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("部门, 查询条件")
	private Long deptId;

	@ApiModelProperty("查询key")
	private String searchKey;
}
