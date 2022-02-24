package org.springblade.modules.file.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.common.constant.RootMappingConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.api.R;
import org.springblade.modules.file.bean.entity.BusFile;
import org.springblade.modules.file.bean.vo.BusFileUploadVO;
import org.springblade.modules.file.bean.vo.BusFileVO;
import org.springblade.modules.file.service.BusFileService;
import org.springblade.modules.file.utils.FileUtils;
import org.springblade.modules.file.wrapper.BusFileWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
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
		BusFileUploadVO uploadVO = FileUtils.upload(file.getInputStream());
		BusFile busFile = new BusFile();
		busFile.setName(file.getOriginalFilename());
		busFile.setUrl(uploadVO.getUrl());
		busFile.setServerUrl(uploadVO.getServerUrl());
		busFile.setSize(file.getSize());
		busFile.setCreateUser(CommonUtil.getUserId());
		busFile.setCreateTime(new Date());
		busFile.setCreateDept(CommonUtil.getDeptId());
		fileService.save(busFile);
		return R.data(BusFileWrapper.build().entityVO(busFile));
	}

	@GetMapping("/download")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "下载文件")
	public void downLoad(@RequestParam("id") Long id,
						 HttpServletRequest request,
						 HttpServletResponse response){
		BusFile busFile = fileService.getById(id);
		if (busFile == null) {
			throw new ServiceException("文件不存在");
		}
		File file = new File(busFile.getServerUrl());
		downloadFile(request, response, file, busFile.getName(), false);
	}

	/**
	 * 下载文件
	 */
	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, File file, String fileName, boolean deleteOnExit) {
		response.setCharacterEncoding(request.getCharacterEncoding());
		response.setContentType("application/octet-stream");
		FileInputStream fis = null;
		ServletOutputStream out = null;
		try {
			fis = new FileInputStream(file);
			fileName = URLEncoder.encode(StrUtil.isBlank(fileName) ? file.getName() : fileName, "utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			out = response.getOutputStream();
			IoUtil.copy(fis, out);
			response.flushBuffer();
		} catch (Exception var14) {

		} finally {
			if (fis != null) {
				try {
					fis.close();
					if (deleteOnExit) {
						file.deleteOnExit();
					}
				} catch (IOException var13) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
