package org.springblade.modules.check.bean.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/17 19:21
 * @Description: 检查法 DTO
 */
@Data
@Api("检查法 DTO")
public class CheckDTO {

	@ApiModelProperty("来源")
	@NotEmpty(message = "来源不能为空")
	@Valid
	private List<CheckResourceDTO> resourceList;


	@ApiModelProperty("种类 0新规格 1工变 2设变 3写误订正 4只变更检查法方法")
	@NotNull(message = "种类不能为空")
	private Integer type;

	@ApiModelProperty("确定内容")
	@NotBlank(message = "确定内容不能为空")
	private String confirmContent;

	@ApiModelProperty("变更内容")
	@NotBlank(message = "变更内容不能为空")
	private String changeContent;

	@ApiModelProperty("变更图片")
	@NotNull(message = "变更图片不能为空")
	private Long changeImageId;

	@ApiModelProperty("部门意见")
	@NotBlank(message = "部门意见不能为空")
	private String deptIdea;

	@ApiModelProperty("检查管理部门意见")
	@NotBlank(message = "检查管理部门意见不能为空")
	private String checkDeptIdea;

	@ApiModelProperty("供应商承认excel文件id")
	@NotNull(message = "供应商承认excel文件id不能为空")
	private Long providerExcelFileId;

	@ApiModelProperty("供应商承认excel文件名称")
	@NotBlank(message = "供应商承认excel文件名称不能为空")
	private String providerExcelFileName;

	@ApiModelProperty("供应商承认签字文件id")
	@NotNull(message = "供应商承认签字文件id不能为空")
	private Long providerSignatureId;

	@ApiModelProperty("供应商承认签字文件名称")
	@NotBlank(message = "供应商承认签字文件名称不能为空")
	private String providerSignatureName;

	@ApiModelProperty("其他附件ids")
	private String extendsFileIds;
}
