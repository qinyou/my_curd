package com.github.qinyou.common.render;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ImageRender
 * @author  chuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class ImageRender extends Render {

    // 这两个参数不同时存在
    private InputStream in;
    private byte[] bytes;

    public ImageRender inputStream(InputStream in) {
        this.in= in;
        return this;
    }
    public ImageRender bytes(byte[] bytes){
        this.bytes= bytes;
        return this;
    }

    @Override
    public void render() {
        response.reset();
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream sos = null;
        try {
            sos = response.getOutputStream();
            if(in!=null){
                byte[] data = new byte[in.available()];
                in.read(data);
                sos.write(data);
            }else{
                sos.write(bytes);
            }
            sos.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RenderException(e);
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
