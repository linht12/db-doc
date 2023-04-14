package com.cn.db.dbdoc.entity;

import lombok.Data;

import java.util.List;

/**
 * @author linht
 */
@Data
public class TableInfo {
    /***
     * 表名
     */
    private String tableName;

    /***
     * 备注信息
     */
    private String tableRemark;

    /***
     * 列
     */
    private List<TableFieldInfo> fields;

    /**
     * 索引信息
     */
    private List<TableKeyInfo> keys;
}
