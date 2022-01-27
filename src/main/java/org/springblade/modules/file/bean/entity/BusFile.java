package org.springblade.modules.file.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 14:09
 * @Description: 文件
 */
@Data
@Api("文件")
@TableName("bus_file")
public class BusFile {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty("主键")
	private String id;

	@ApiModelProperty("文件名称")
	private String name;

	@ApiModelProperty("访问路径")
	private String url;

	@ApiModelProperty("文件大小")
	private Long size;

	@ApiModelProperty("创建用户")
	private Long createUser;

	@ApiModelProperty("创建时间")
	private Date createTime;

	@ApiModelProperty("创建部门")
	private Long createDept;
}
