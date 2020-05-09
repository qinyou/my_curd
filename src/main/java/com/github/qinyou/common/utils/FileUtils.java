package com.github.qinyou.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件操作工具类
 *
 * @author zhangchuang
 */
@Slf4j
public class FileUtils extends org.apache.commons.io.FileUtils {

    /**
     * 通过文件全路径 获得文件的 MIME(contentType)类型
     * jdk7
     *
     * @param absolutePath 文件全路径
     * @return
     */
    public static String getMime(String absolutePath) {
        Path path = Paths.get(absolutePath);
        String contentType = null;
        try {
            contentType = java.nio.file.Files.probeContentType(path);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }


    public static void deleteFile(File file) {
        if (!file.delete()) {
            log.error("文件{} 未删除成功.", file.getAbsolutePath());
        }
    }
}
