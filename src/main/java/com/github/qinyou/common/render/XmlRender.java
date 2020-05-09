package com.github.qinyou.common.render;

import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.RenderException;
import freemarker.template.Template;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 渲染 xml，通过 freemarker 模板
 */
public class XmlRender extends FreeMarkerRender {
    private static final String CONTENT_TYPE = "text/xml; charset=" + getEncoding();

    public XmlRender(String view) {
        super(view);
    }

    @Override
    public void render() {
        response.setContentType(CONTENT_TYPE);

        Enumeration<String> attrs = request.getAttributeNames();
        Map<String, Object> root = new HashMap<>();
        while (attrs.hasMoreElements()) {
            String attrName = attrs.nextElement();
            root.put(attrName, request.getAttribute(attrName));
        }

        Writer writer = null;
        try {
            writer = response.getWriter();
            Template template = getConfiguration().getTemplate(view);
            template.process(root, writer);
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
