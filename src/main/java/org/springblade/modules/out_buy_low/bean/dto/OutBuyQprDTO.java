package org.springblade.modules.out_buy_low.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.log.exception.ServiceException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @Author: xiaoxia
 * @Date: 2022/1/28 10:54
 * @Description: 外构件不良QPR
 */

@Data
@Api("外构件不良QPR")
public class OutBuyQprDTO {

	@ApiModelProperty("标题")
	@NotBlank(message = "标题不能为空")
	private String title;

	@ApiModelProperty("不良编号")
	private Long code;

	@ApiModelProperty("品番号")
	@NotBlank(message = "品番号不能为空")
	private String designation;

	@ApiModelProperty("品名")
	@NotBlank(message = "品名不能为空")
	private String name;

	@ApiModelProperty("发生地点 0TNGA#1 1TNGA#2 .....")
	@NotNull(message = "发生地点不能为空")
	private Long triggerAddress;

	@ApiModelProperty("不良数量")
	private Long findQuantity;

	@ApiModelProperty("发现时间")
	@NotNull(message = "发现时间不能为空")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	private Date findTime;

	@ApiModelProperty("不良等级 0R 1S 2A 3B 4C 5批量 6停线")
	@NotNull(message = "不良等级不能为空")
	private Long level;

	@ApiModelProperty("供应商名称")
	@NotBlank(message = "供应商名称不能为空")
	private String dutyDept;

	@ApiModelProperty("事件概要")
	@NotBlank(message = "事件概要不能为空")
	private String eventRemark;

	@ApiModelProperty("拜托事项")
	@NotBlank(message = "拜托事项不能为空")
	private String pleaseContent;

	@ApiModelProperty("不良图示/测试报告")
	@NotBlank(message = "不良图片不能为空")
	private String imgReportIds;

	@ApiModelProperty("机型,逗号隔开 0TNGA2.0")
	@NotNull(message = "机型不能为空")
	private Integer apparatusType;

	@ApiModelProperty("处理 0返还 1保留 2废弃 3其他")
	@NotNull(message = "不良品处理不能为空")
	private Long dispostType;

	@ApiModelProperty("处理")
	private String dispost;

	@ApiModelProperty("工序内不良信息标识 0非工序内不良 1工序内不良审批 2工序内不良QPR录入")
	private Integer processLowFlag;

	@ApiModelProperty("工序内不良Id")
	private Long processLowId;

	public void validate() {
		if (new Long(0).equals(dispostType) || new Long(3).equals(dispostType)) {

			if (StrUtil.isBlank(dispost)) {
				throw  new ServiceException("请填写不良品处理");
			}
		}
	}
}
