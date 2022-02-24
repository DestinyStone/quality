package org.springblade.modules.check.bean.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.modules.account.bean.core.Version;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/24 20:53
 * @Description:
 */
@Data
@Api("检查法版本台账 视图类")
public class CheckAccountVersionVO extends Version {
	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("供应商承认excel文件id")
	private Long providerExcelFileId;

	@ApiModelProperty("供应商承认excel文件名称")
	private String providerExcelFileName;

	@ApiModelProperty("供应商承认签字文件id")
	private Long providerSignatureId;

	@ApiModelProperty("供应商承认签字文件名称")
	private String providerSignatureName;

	@ApiModelProperty("丰田商承认签字文件id")
	private Long toyotaExcelFileId;

	@ApiModelProperty("丰田商承认签字文件名称")
	private String toyotaExcelFileName;

}
