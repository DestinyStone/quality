package org.springblade.modules.file.utils;

import lombok.SneakyThrows;
import org.springblade.core.log.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Author: xiaoxia
 * @Date: 2022/1/27 14:21
 * @Description:
 */
@Component
public class FileUtils {

	private static String fileServer;

	private static String fileRootPath;

	/**
	 * 上传文件
	 * @param file
	 * @return 文件访问路径
	 */
	@SneakyThrows
	public static String upload(File file) {
		FileInputStream inputStream = new FileInputStream(file);
		return upload(inputStream);
	}

	/**
	 * 上传文件
	 * @param inputStream
	 * @return 文件访问路径
	 */
	@SneakyThrows
	public static String upload(InputStream inputStream) {
		String fileName = getRandomFileName();
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(getOutPath(fileName));
			byte[] bytes = new byte[inputStream.available()];
			int len = -1;
			while ((len = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, len);
			}
			return getRemotePath(fileName);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("文件上传异常");
		}finally {
			if (outputStream != null) {
				outputStream.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	@Value("${file.upload.server}")
	public void setFileServer(String fileServer) {
		FileUtils.fileServer = fileServer;
	}

	@Value("${file.upload.root.path}")
	public void setFileRootPath(String fileRootPath) {
		FileUtils.fileRootPath = fileRootPath;
	}

	private static String getRemotePath(String fileName) {
		return fileServer + "/" + fileName;
	}

	private static String getOutPath(String fileName) {
		return fileRootPath + File.separator + fileName;
	}

	private static String getRandomFileName() {
		return UUID.randomUUID().toString();
	}
}
