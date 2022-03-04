package org.springblade.modules.di.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.modules.out_buy_low.bean.dto.ResourceDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/3 18:26
 * @Description:
 */
@Data
@Api("di配置 DTO")
public class DiConfigSubmitDTO {
	@ApiModelProperty("来源")
	@NotEmpty(message = "来源不能为空")
	@Valid
	private List<ResourceDTO> resourceList;

	@ApiModelProperty("上报周期 0每月1号 1特定 2立即上报")
	@NotNull(message = "上报周期不能为空")
	private String cycleType;

	@ApiModelProperty("上报周期时间")
	private Date cycleTime;

	@ApiModelProperty("超期提醒类型 0立即提醒 1天数")
	@NotNull(message = "超期提醒类型不能为空")
	private Integer pastType;

	@ApiModelProperty("超期提醒")
	private Integer pastDay;

	@ApiModelProperty("状态 0禁用 1启用")
	private Integer status;

	public void validate() {
		if (cycleType.contains("1") && cycleTime == null) {
			throw new ServiceException("请输入上报周期特定时间");
		}

		if (pastType == 1 && pastDay == null) {
			throw new ServiceException("请输入超期提醒天数");
		}
	}
}
