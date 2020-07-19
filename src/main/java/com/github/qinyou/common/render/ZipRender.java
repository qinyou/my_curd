package com.github.qinyou.common.render;

import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.utils.ZipUtils;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * zip render, 字符串 + 文件名 生成压缩包
 * @author chuang
 */
@SuppressWarnings({"unused","Duplicates"})
@Slf4j
public class ZipRender extends Render {
    private final static String CONTENT_TYPE = "application/x-zip-compressed;charset=" + getEncoding();

    private String fileName;      //  zip 压缩文件名

    // data 和  dataIn 不同时存在
    private List<String> data;        // 字符串数据 集合
    private List<InputStream> dataIn; // 输入流数据 集合

    // filenames 可以有层级
    private List<String> filenames; // 文件名 集合

    public static ZipRender me() {
        return new ZipRender();
    }

    public ZipRender fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ZipRender data(List<String> data) {
        this.data = data;
        return this;
    }

    public ZipRender dataIn(List<InputStream> dataIn) {
        this.dataIn = dataIn;
        return this;
    }

    public ZipRender filenames(List<String> filenames) {
        this.filenames = filenames;
        return this;
    }

    @Override
    public void render() {
        response.reset();
        fileName = WebUtils.buildDownname(request, fileName);
        response.setHeader("Content-disposition", "attachment;" + fileName);
        response.setContentType(CONTENT_TYPE);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            if(data!=null ){
                ZipUtils.toZip(data, filenames, os);
            }else if(dataIn!=null){
                ZipUtils.toZipByInputStream(filenames,dataIn, os);
            }else{
                log.error("data 数据缺失");
                response.reset();
                response.setContentType("html/text");
                response.getWriter().println("data 参数缺失");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RenderException(e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
