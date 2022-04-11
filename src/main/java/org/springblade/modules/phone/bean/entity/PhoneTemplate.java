package org.springblade.modules.phone.bean.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/15 14:16
 * @Description:
 */
@Data
@TableName("bus_phone_template")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "邮件模板", description = "邮件模板")
public class PhoneTemplate extends BaseEntity {

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

	/**
	 * 联系code
	 */
	@ApiModelProperty(value = "联系code")
	private String concatCode;

}
