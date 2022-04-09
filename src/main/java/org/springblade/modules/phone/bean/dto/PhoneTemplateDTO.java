package org.springblade.modules.phone.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/15 14:16
 * @Description:
 */
@Data
@ApiModel(value = "邮件模板", description = "邮件模板")
public class PhoneTemplateDTO {

    /**
     * 邮件标题
     */
    @ApiModelProperty(value = "邮件标题")
    @NotEmpty(message = "邮件标题不能为空")
    private String title;

    /**
     * 邮件编码
     */
    @ApiModelProperty(value = "邮件编码")
    @NotEmpty(message = "邮件编号不能为空")
    private String code;

    /**
     * 邮件内容
     */
    @ApiModelProperty(value = "邮件内容")
    @NotEmpty(message = "邮件内容不能为空")
    private String content;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

	@ApiModelProperty(value = "邮件参数")
	@Valid
    private List<PhoneTemplateParamsDTO> params;
}
