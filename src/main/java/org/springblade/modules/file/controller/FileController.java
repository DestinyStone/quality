package org.springblade.modules.file.controller;

import io.swagger.annotations.Api;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.api.R;
import org.springblade.modules.file.bean.entity.BusFile;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.file.service.BusFileService;
import org.springblade.modules.file.utils.FileUtils;
import org.springblade.modules.file.wrapper.BusFileWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 14:09
 * @Description: 文件接口
 */
@RequestMapping(RootMappingConstant.root + "/file")
@RestController
@Api(value = "文件接口", tags = "文件接口")
public class FileController {

	@Autowired
	private BusFileService fileService;

	@GetMapping("/detail")
	public R<BusFileVO> detail(@RequestParam("id") Long id) {
		BusFile busFile = fileService.getById(id);
		if (busFile == null) {
			throw new ServiceException("文件不存在");
		}
		return R.data(BusFileWrapper.build().entityVO(busFile));
	}

	@PostMapping("/upload")
	public R<BusFileVO> upload(MultipartFile file) throws IOException {
		String fileSrc = FileUtils.upload(file.getInputStream());
		BusFile busFile = new BusFile();
		busFile.setName(file.getOriginalFilename());
		busFile.setUrl(fileSrc);
		busFile.setSize(file.getSize());
		busFile.setCreateUser(CommonUtil.getUserId());
		busFile.setCreateTime(new Date());
		busFile.setCreateDept(CommonUtil.getDeptId());
		fileService.save(busFile);
		return R.data(BusFileWrapper.build().entityVO(busFile));
	}
}
