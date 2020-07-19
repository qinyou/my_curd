package com.github.qinyou.common.render;

import com.github.qinyou.common.utils.WebUtils;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;

/**
 * excel 导出, 结合 easypoi 使用
 * @author chuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class ExcelRender extends Render {

    private final static String CONTENT_TYPE = "application/msexcel;charset=" + getEncoding();

    private String fileName;
    private Workbook workbook;

    private ExcelRender(Workbook workbook) {
        this.workbook = workbook;
    }

    public static ExcelRender me(Workbook workbook) {
        return new ExcelRender(workbook);
    }

    public ExcelRender fileName(String fileName) {
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
            workbook.write(os);
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
