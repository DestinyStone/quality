package org.springblade.modules.di.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/3 18:16
 * @Description:
 */
@Data
@Api("di配置 实体")
@TableName("bus_di_config")
public class DiConfig {

	private Long id;

	@ApiModelProperty("来源id")
	private Long resourceId;

	@ApiModelProperty("来源类型 0qpr 1low")
	private Integer resourceType;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品名")
	private String name;

	@ApiModelProperty("供应商名称")
	private String dutyDept;

	@ApiModelProperty("最后版本文件id")
	private Long lastFileId;

	@ApiModelProperty("最后版本文件名")
	private String lastFileName;

	@ApiModelProperty("上报周期 0每月1号 1特定 2立即上报")
	private String cycleType;

	@ApiModelProperty("上报周期时间")
	private Date cycleTime;

	@ApiModelProperty("超期提醒类型 0立即提醒 1天数")
	private Integer pastType;

	@ApiModelProperty("超期提醒")
	private Integer pastDay;

	@ApiModelProperty("状态 0禁用 1启用")
	private Integer status;

	@ApiModelProperty("更新部门")
	private Long updateDept;

	@ApiModelProperty("更新用户")
	private Long updateUser;

	@ApiModelProperty("更新时间")
	private Date updateTime;

}
