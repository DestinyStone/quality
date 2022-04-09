package org.springblade.modules.phone.bean.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiaoxia
 * @Date: 2022/3/15 14:16
 * @Description:
 */
@Data
@TableName("bus_email_template_param")
@ApiModel(value = "EmailTemplateParam对象", description = "邮件模板参数")
public class PhoneTemplateParamVO {

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键")
	private Long id;

	/**
	 * 中文名称
	 */
	@ApiModelProperty(value = "中文名称")
	private String label;

	/**
	 * 英文名称
	 */
	@ApiModelProperty(value = "英文名称")
	private String name;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 默认值
	 */
	@ApiModelProperty(value = "默认值")
	private String defaultValue;

	/**
	 * 模板ID
	 */
	@ApiModelProperty(value = "模板ID")
	private Long templateId;

}
