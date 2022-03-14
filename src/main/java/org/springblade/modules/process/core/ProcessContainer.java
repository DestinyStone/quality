package org.springblade.modules.process.core;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.utils.Func;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/29 00:38
 * @Description:
 */
@Data
public class ProcessContainer {

	@ApiModelProperty("业务Id")
	private String busId;

	@ApiModelProperty("流程描述")
	private String remark;

	@ApiModelProperty("排序")
	private Integer sort;

	@ApiModelProperty("允许操作部门 0匹配任何部门")
	private String accessDept;

	@ApiModelProperty("允许操作的角色 0匹配任何角色")
	private String accessRole;

	@ApiModelProperty("状态")
	private Integer status;

	@ApiModelProperty("标识")
	private String flag;

	@ApiModelProperty("服务标识")
	private String serverFlag;

	public ProcessContainer() {
	}

	public ProcessContainer(String busId, List<String> accessDept, List<String> accessRole, String remark, Integer sort, String flag, String serverFlag, Integer status) {
		this.busId = busId;
		this.remark = remark;
		this.sort = sort;
		this.accessDept = Func.join(accessDept);
		this.accessRole = Func.join(accessRole);
		this.status = status;
		this.flag = flag;
		this.serverFlag = serverFlag;
	}

	public ProcessContainer(String busId, String accessDept, List<String> accessRole, String remark, Integer sort, String flag, String serverFlag, Integer status) {
		this(busId, Arrays.asList(accessDept), accessRole, remark, sort, flag, serverFlag, status);
	}

	public ProcessContainer(String busId, List<String> accessDept, String accessRole, String remark, Integer sort, String flag, String serverFlag, Integer status) {
		this(busId, accessDept, Arrays.asList(accessRole), remark, sort, flag, serverFlag, status);
	}

	public ProcessContainer(String busId, String accessDept, String accessRole, String remark, Integer sort, String flag, String serverFlag, Integer status) {
		this(busId,  Arrays.asList(accessDept), Arrays.asList(accessRole), remark, sort, flag, serverFlag, status);
	}
}
