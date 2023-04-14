package com.cn.db.dbdoc.generator;

import com.cn.db.dbdoc.enums.GenerateType;
import jakarta.annotation.PostConstruct;

import java.io.OutputStream;

/**
 * @author linht
 */
public interface GeneratorService {


    @PostConstruct
    default void init() {
        GeneratorFactory.register(this);
    }
    /**
     * 获取该类型
     * @return
     */
    GenerateType type();

    /***
     * 生成数据库文档
     *
     * @throws Exception
     */
    void generateDbDoc(Long id, OutputStream outputStream) throws Exception;



}
