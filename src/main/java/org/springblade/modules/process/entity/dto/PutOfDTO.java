package org.springblade.modules.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/31 00:04
 * @Description: 延迟处理 dto
 */
@Data
public class PutOfDTO {

	@ApiModelProperty("流程主键")
	@NotNull(message = "流程主键不能为空")
	private Long bpmId;

	@ApiModelProperty("结束时间")
	@NotNull(message = "结束时间不能为空")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	private Date endTime;

	@ApiModelProperty("延迟描述")
	@NotBlank(message = "延迟描述不能为空")
	private String remark;
}
