package com.github.qinyou.common.web;

import com.github.qinyou.AppConfig;
import com.github.qinyou.common.interceptor.PermissionInterceptor;
import com.github.qinyou.common.utils.FileUtils;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 公共文件上传路由
 */
@Clear(PermissionInterceptor.class)
@Slf4j
public class FileController extends BaseController {

    private final static String PARAM_FILE_EMPTY = "文件参数为空";
    private final static String FILE_TYPE_NOT_LIMIT = " 后缀文件禁止上传";
    private final static String FILE_EXIST = "文件已存在";

    private static Set<String> limitTypes = new HashSet<>();
    private static List<String> imageTypeLimit;
    private static String imagePath;
    private static List<String> mediaTypeLimit;
    private static String mediaPath;
    private static List<String> officeTypeLimit;
    private static String officePath;
    private static List<String> fileTypeLimit;
    private static String filePath;

    static {
        imagePath = AppConfig.configProp.get("file.imagePath");
        imageTypeLimit = Arrays.asList(AppConfig.configProp.get("file.imageType").split(","));
        limitTypes.addAll(imageTypeLimit);

        mediaPath = AppConfig.configProp.get("file.mediaPath");
        mediaTypeLimit = Arrays.asList(AppConfig.configProp.get("file.mediaType").split(","));
        limitTypes.addAll(mediaTypeLimit);

        officePath = AppConfig.configProp.get("file.officePath");
        officeTypeLimit = Arrays.asList(AppConfig.configProp.get("file.officeType").split(","));
        limitTypes.addAll(officeTypeLimit);

        filePath = AppConfig.configProp.get("file.filePath");
        fileTypeLimit = Arrays.asList(AppConfig.configProp.get("file.fileType").split(","));
        limitTypes.addAll(fileTypeLimit);
    }

    /**
     * 单文件上传
     */
    public void upload() throws IOException {
        UploadFile uploadFile = getFile("file");

        if (uploadFile == null) {
            renderFail(PARAM_FILE_EMPTY);
            return;
        }

        String originalFileName = uploadFile.getOriginalFileName();
        String extension = FilenameUtils.getExtension(originalFileName);

        // 文件类型非法
        if (!checkFileType(extension)) {
            FileUtils.deleteFile(uploadFile.getFile());
            renderFail(extension + FILE_TYPE_NOT_LIMIT);
            return;
        }

        // 文件保存
        String relativePath = fileRelativeSavePath(extension);
        File saveFile = new File(PathKit.getWebRootPath() + "/" + relativePath);
        if (saveFile.exists()) {
            FileUtils.deleteFile(uploadFile.getFile());
            renderFail(originalFileName + FILE_EXIST);
            return;
        }


        FileUtils.copyFile(uploadFile.getFile(), saveFile);
        FileUtils.deleteFile(uploadFile.getFile());

        UploadResult uploadResult = new UploadResult();
        uploadResult.setName(originalFileName);
        uploadResult.setPath(relativePath);
        long sizeL = saveFile.length();
        uploadResult.setSizeL(sizeL);
        uploadResult.setSize(FileUtils.byteCountToDisplaySize(sizeL));
        StringBuffer url = getRequest().getRequestURL();
        String uri = url.delete(url.length() - getRequest().getRequestURI().length(), url.length()).append(getRequest().getServletContext().getContextPath()).append("/").toString();
        uploadResult.setUri(uri + relativePath);

        Ret ret = Ret.create().setOk().set("data", uploadResult);
        renderJson(ret);
    }

    /**
     * 检查文件类型是否合法
     *
     * @param extension 文件后缀
     * @return true 合法，false 非法
     */
    private boolean checkFileType(String extension) {
        boolean flag = false;
        return limitTypes.contains(extension);
    }


    /**
     * 获得文件存盘 相对 路径
     *
     * @param extension 文件后缀
     * @return
     */
    private String fileRelativeSavePath(String extension) {
        String path = "/" + new DateTime(new Date()).toString("yyyy_MM_dd");

        if (imageTypeLimit.contains(extension)) {
            path = imagePath + path;
        } else if (mediaTypeLimit.contains(extension)) {
            path = mediaPath + path;
        } else if (officeTypeLimit.contains(extension)) {
            path = officePath + path;
        } else if (fileTypeLimit.contains(extension)) {
            path = filePath + path;
        } else {
            throw new RuntimeException(extension + " 未找到存盘相对路径");
        }

        // 时分秒毫秒+随机数
        path = path + "/" + IdUtils.id() + "." + extension;
        return path;
    }

    @Data
    public class UploadResult {
        private String name;   // 文件原名
        private String path;   // 文件路径
        private String uri;    // 文件web下路径
        private String size;   // 文件大小 显示名 例 10KB、2MB、3GB
        private Long sizeL;    // 文件大小
    }
}



