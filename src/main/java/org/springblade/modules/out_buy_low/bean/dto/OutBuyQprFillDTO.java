package org.springblade.modules.out_buy_low.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: DestinyStone
 * @Date: 2022/2/2 19:10
 * @Description:
 */
@Data
public class OutBuyQprFillDTO {

	@ApiModelProperty("属性 0单发 1散发 2批量")
	@NotNull(message = "属性不能为空")
	private Integer fillPropertiesType;

	@ApiModelProperty("不良类型 0漏工序 1欠品误品 2异音 3异物 4外观 5测漏NG 6尺寸 7其他")
	@NotNull(message = "不良类型不能为空")
	private Integer fillType;

	@ApiModelProperty("流出原因分类 0检出力低 2防错失效 3标准作业不足 4异常处置不当 5其他")
	@NotNull(message = "流出原因分类不能为空")
	private Integer fillOutCauseType;

	@ApiModelProperty("发生原因分类 0夹装异常 1加功条件管理不足 2标准作业不足 3异常处置不当 4变化点管理不足 5其他")
	@NotNull(message = "发生原因分类不能为空")
	private Integer fillTriggerCauseType;

	@ApiModelProperty("判断结果 0供应商责任 1责任不明结案 2生管责任 3其他直接结案 4是否其他结案")
	@NotNull(message = "判断结果不能为空")
	private Integer fillJudgeResult;

	@ApiModelProperty("描述")
	private String fillRemark;
}
