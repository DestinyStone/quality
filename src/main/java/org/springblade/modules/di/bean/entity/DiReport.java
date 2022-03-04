package org.springblade.modules.di.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/4 12:36
 * @Description:
 */
@Data
@Api("di上报 实体")
@TableName("bus_di_report")
public class DiReport {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("管理编号")
	private String code;

	@ApiModelProperty("di配置Id")
	private Long diConfigId;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品名")
	private String name;

	@ApiModelProperty("供应商")
	private String dutyDept;

	@ApiModelProperty("业务类型 0DI数据上报 1无需上报")
	private Integer businessType;

	@ApiModelProperty("审批状态")
	private Integer bpmStatus;

	@ApiModelProperty("0 未上报 1已上报")
	private Integer status;

	@ApiModelProperty("上报excel文件Id")
	private Long dataExcelFileId;

	@ApiModelProperty("上报excel文件名称")
	private String dataExcelFileName;

	@ApiModelProperty("上报签名文件Id")
	private Long dataSignateFileId;

	@ApiModelProperty("上报签名文件名称")
	private String dataSignateFileName;

	@ApiModelProperty("丰田承认文件Id")
	private Long toyoDataFileId;

	@ApiModelProperty("丰田承认文件名称")
	private String toyoDataFileName;

	@ApiModelProperty("无需上报理由")
	private String noReportRemark;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("上报时间")
	private Date reportTime;

	@ApiModelProperty("上报用户")
	private Long reportUser;

	@ApiModelProperty("提醒天数")
	private Integer pastDay;
}
