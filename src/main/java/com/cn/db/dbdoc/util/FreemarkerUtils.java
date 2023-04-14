package com.cn.db.dbdoc.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * @author linht
 */
@Slf4j
public class FreemarkerUtils {
    public static Template getTemplate(String ftlName) throws Exception {
        try {
            // 通过Freemaker的Configuration读取相应的ftl
            Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate(ftlName, IOUtils.toString(FreemarkerUtils.class.getClassLoader().getResourceAsStream(ftlName), Charset.defaultCharset()));
            cfg.setEncoding(Locale.CHINA, "utf-8");
            // 设定去哪里读取相应的ftl模板文件
            cfg.setTemplateLoader(stringLoader);
            // 在模板文件目录中找到名称为name的文件
            Template temp = cfg.getTemplate(ftlName);
            return temp;
        } catch (IOException e) {
            log.error("",e);
        }
        return null;
    }
}
