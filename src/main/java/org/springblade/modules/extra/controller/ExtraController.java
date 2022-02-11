package org.springblade.modules.extra.controller;

import io.swagger.annotations.Api;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.tool.api.R;
import org.springblade.modules.file.bean.entity.BusFile;
import org.springblade.modules.file.bean.vo.BusFileUploadVO;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.file.utils.FileUtils;
import org.springblade.modules.file.wrapper.BusFileWrapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/2/11 17:19
 * @Description: 额外的接口
 */
@RequestMapping(RootMappingConstant.root + "/extra")
@RestController
@Api(value = "额外的接口", tags = "额外的接口")
public class ExtraController {
	@PostMapping("/upload")
	public void upload(MultipartFile file) throws IOException {
		FileUtils.upload(file.getInputStream(), System.currentTimeMillis() + ".jpg");
	}
}
