package com.cn.db.dbdoc.controller;

import com.cn.db.dbdoc.generator.GeneratorFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

/**
 * 导出
 * @author linht
 */
@RestController
@RequestMapping("/export")
public class ExportController {

    @GetMapping("/excel")
    public void export(@RequestParam(value = "id") Long id,  HttpServletResponse response) throws Exception {
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("数据库字典", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        GeneratorFactory.get("excel").generateDbDoc(id, response.getOutputStream());
    }


    @GetMapping("/word")
    public void word(@RequestParam(value = "id") Long id, HttpServletResponse response) throws Exception {
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("数据库字典", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".doc");
        GeneratorFactory.get("word").generateDbDoc(id, response.getOutputStream());
    }
}
