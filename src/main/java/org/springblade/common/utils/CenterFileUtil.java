package org.springblade.common.utils;

import java.io.File;

/**
 * @Author: DestinyStone
 * @Date: 2022/1/30 13:03
 * @Description: 文件工具类
 */
public class CenterFileUtil {

    public static final String ROOT = System.getProperty("user.dir");

    /**
     * 获取文件全路径
     *
     * @param fileName 文件名称
     * @return
     */
    public static String getFilePath(String fileName) {
        return ROOT + File.separator + fileName;
    }

    /**
     * 获取文件
     *
     * @param fileName
     * @return
     */
    public static File getFile(String fileName) {
        return new File(getFilePath(fileName));
    }

}
