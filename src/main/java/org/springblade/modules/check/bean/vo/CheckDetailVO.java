package org.springblade.modules.check.bean.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.modules.file.bean.vo.BusFileVO;

import java.util.Date;
import java.util.List;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/23 16:00
 * @Description:
 */
@Data
@Api("检查法 视图类")
public class CheckDetailVO {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键")
	private Long id;

	@ApiModelProperty("品番号")
	private String designation;

	@ApiModelProperty("品番名")
	private String name;

	@ApiModelProperty("编码")
	private String code;

	@ApiModelProperty("来源id")
	private Long resourceId;

	@ApiModelProperty("来源类型 0qpr 1low")
	private Integer resourceType;

	@ApiModelProperty("种类 0新规格 1工变 2设变 3写误订正 4只变更检查法方法")
	private Integer type;

	@ApiModelProperty("确定内容")
	private String confirmContent;

	@ApiModelProperty("变更内容")
	private String changeContent;

	@ApiModelProperty("变更图片")
	private Long changeImageId;

	@ApiModelProperty("图片url")
	private String changeImageUrl;

	@ApiModelProperty("图片文件")
	private BusFileVO changeImageFile;

	@ApiModelProperty("部门意见")
	private String deptIdea;

	@ApiModelProperty("检查管理部门意见")
	private String checkDeptIdea;

	@ApiModelProperty("供应商承认excel文件id")
	private Long providerExcelFileId;

	@ApiModelProperty("供应商承认excel文件名称")
	private String providerExcelFileName;

	@ApiModelProperty("供应商承认excel文件")
	private BusFileVO providerExcelFile;

	@ApiModelProperty("供应商承认签字文件id")
	private Long providerSignatureId;

	@ApiModelProperty("供应商承认签字文件名称")
	private String providerSignatureName;

	@ApiModelProperty("供应商承认签字文件")
	private BusFileVO providerSignatureFile;

	@ApiModelProperty("丰田商承认签字文件id")
	private Long toyotaExcelFileId;

	@ApiModelProperty("丰田商承认签字文件名称")
	private String toyotaExcelFileName;

	@ApiModelProperty("丰田商承认签字文件")
	private BusFileVO toyotaExcelFile;

	@ApiModelProperty("其他附件ids")
	private String extendsFileIds;

	@ApiModelProperty("其他附件")
	private List<BusFileVO> extendsFiles;

	@ApiModelProperty("0新增检查法 1修改检查法")
	private Integer bpmNode;

	@ApiModelProperty("审批状态")
	private Integer bpmStatus;

	@ApiModelProperty("状态")
	private Integer status;

	@ApiModelProperty("创建部门")
	private Long createDept;

	@ApiModelProperty("创建部门名称")
	private String createDeptName;
}
