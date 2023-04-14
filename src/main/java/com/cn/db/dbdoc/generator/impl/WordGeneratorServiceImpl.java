package com.cn.db.dbdoc.generator.impl;

import com.cn.db.dbdoc.entity.TableInfo;
import com.cn.db.dbdoc.enums.GenerateType;
import com.cn.db.dbdoc.generator.GeneratorService;
import com.cn.db.dbdoc.servjce.DbInfoService;
import com.cn.db.dbdoc.util.FreemarkerUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Template;


/**
 * @author linht
 */
@Service
@Slf4j
public class WordGeneratorServiceImpl implements GeneratorService {
    @Resource
    private DbInfoService dbInfoService;

    @Value("${app.word.templateFilePath:wordTemplate.ftl}")
    private String templateFileName;


    @Override
    public GenerateType type() {
        return GenerateType.word;
    }

    @Override
    public void generateDbDoc(Long id, OutputStream outputStream) throws Exception {
        String databaseName = dbInfoService.databaseName(id);
        List<TableInfo> tableInfos = dbInfoService.tableInfoList(id);
        // 获取模板
        Template template = FreemarkerUtils.getTemplate(templateFileName);
        Map<String, Object> map = new HashMap<>(2);
        map.put("tableInfos", tableInfos);
        map.put("databaseName", databaseName);
        // 根据模板生成文件
        try( StringWriter sw = new StringWriter()) {
            template.process(map, sw);
            IOUtils.write(sw.toString(), outputStream, Charset.defaultCharset());
        }
    }
}
