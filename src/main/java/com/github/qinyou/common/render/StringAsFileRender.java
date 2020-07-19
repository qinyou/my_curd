package com.github.qinyou.common.render;

import com.github.qinyou.common.utils.WebUtils;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 字符串文本下载为文件
 * @author chuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class StringAsFileRender extends Render {
    private final static String CONTENT_TYPE = "application/*;charset=" + getEncoding();

    private String fileName;
    private String content;

    private StringAsFileRender(String content) {
        this.content = content;
    }

    public static StringAsFileRender me(String content) {
        return new StringAsFileRender(content);
    }

    public StringAsFileRender fileName(String fileName) {
        this.fileName = fileName;
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
            os.write(content.getBytes(getEncoding()));
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
