package com.cn.db.dbdoc.entity;

import lombok.Data;

import java.util.List;


/**
 * @author linht
 */
@Data
public class TableKeyInfo {
    public static final String PRIMARY_KEY = "PRIMARY";


    /***
     * 索引名称
     */
    private String name;

    /***
     * 包含那些字段
     */
    private List<String> columns;

    /***
     * 是否唯一
     */
    private Boolean unique;

    /***
     * 索引类型
     */
    private String indexType;

    /***
     * 索引注释
     */
    private String indexComment;
}
