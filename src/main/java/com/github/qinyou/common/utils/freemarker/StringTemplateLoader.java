package com.github.qinyou.common.utils.freemarker;

import freemarker.cache.TemplateLoader;

import java.io.Reader;
import java.io.StringReader;

public class StringTemplateLoader implements TemplateLoader {
    private String template;

    public StringTemplateLoader(String template) {
        this.template = template;
        if (template == null) {
            this.template = "";
        }
    }

    public void closeTemplateSource(Object templateSource) {
        ((StringReader) templateSource).close();
    }

    public Object findTemplateSource(String name) {
        return new StringReader(template);
    }

    public long getLastModified(Object templateSource) {
        return 0;
    }

    public Reader getReader(Object templateSource, String encoding) {
        return (Reader) templateSource;
    }
}
