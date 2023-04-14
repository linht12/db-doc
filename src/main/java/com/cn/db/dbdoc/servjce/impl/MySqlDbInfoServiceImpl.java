package com.cn.db.dbdoc.servjce.impl;

import com.alibaba.fastjson.JSONObject;
import com.cn.db.dbdoc.entity.DbEntity;
import com.cn.db.dbdoc.entity.TableFieldInfo;
import com.cn.db.dbdoc.entity.TableInfo;
import com.cn.db.dbdoc.entity.TableKeyInfo;
import com.cn.db.dbdoc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author linht
 */
@Slf4j
@Service
public class MySqlDbInfoServiceImpl extends DbInfoServiceImpl {


    @Override
    public String databaseName(DbEntity dbEntity) {
        List<Map<String, Object>> list = DbUtils.executeQuery(dbEntity, "select database() AS tableName", "tableName");
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("查询失败,暂无数据");
        }
        return list.get(0).get("tableName") + "";
    }


    @Override
    public List<TableInfo> tableInfoList(DbEntity dbEntity) {
        //查询表信息
        List<TableInfo> tableInfos = queryTableInfo(dbEntity);
        if (CollectionUtils.isEmpty(tableInfos)) {
            return tableInfos;
        }
        tableInfos.stream().forEach((tableInfo) -> {
            tableInfo.setFields(this.queryFields(dbEntity, tableInfo.getTableName()));
            tableInfo.setKeys(this.queryKeys(dbEntity, tableInfo.getTableName(), this.databaseName(dbEntity)));
        });
        return tableInfos;
    }

    private List<TableInfo> queryTableInfo(DbEntity dbEntity) {
        List<Map<String, Object>> list = DbUtils.executeQuery(dbEntity, "select table_name AS table_name ,table_comment As tableRemark from information_schema.tables where table_schema = database()", "table_name", "tableRemark");
        return list.stream().map(e -> JSONObject.parseObject(JSONObject.toJSONString(e), TableInfo.class)).collect(Collectors.toList());
    }

    /**
     * 查询字段
     *
     * @return
     */
    private List<TableFieldInfo> queryFields(DbEntity dbEntity, String tableName) {
        String[] columns = new String[]{"field", "remark", "defaultValue", "nullAble", "type", "key", "extra"};
        String sql = "select COLUMN_NAME AS field, COLUMN_COMMENT AS remark,COLUMN_DEFAULT AS defaultValue,IS_NULLABLE AS nullAble,COLUMN_TYPE AS type,COLUMN_KEY `key`,EXTRA AS extra from information_schema.columns where table_schema =database() and table_name ='" + tableName + "' ";
        List<Map<String, Object>> list = DbUtils.executeQuery(dbEntity, sql, columns);
        return list.stream().map(e -> JSONObject.parseObject(JSONObject.toJSONString(e), TableFieldInfo.class)).collect(Collectors.toList());
    }

    private List<TableKeyInfo> queryKeys(DbEntity dbEntity, String tableName, String database) {
        String sql = "show keys  from " + database + "." + tableName;
        String[] columns = new String[]{"Key_name", "Column_name", "Index_type", "Non_unique", "Index_comment"};
        List<Map<String, Object>> list = DbUtils.executeQuery(dbEntity, sql, columns);
        Map<String, TableKeyInfo> keyMap = new HashMap<>(5);
        for (Map<String, Object> rawKeyInfo : list) {
            TableKeyInfo tableKeyInfo = keyMap.get(rawKeyInfo.get("Key_name").toString());
            String columnName = rawKeyInfo.get("Column_name").toString();
            if (tableKeyInfo == null) {
                tableKeyInfo = new TableKeyInfo();
                ArrayList<String> columnsList = new ArrayList<>();
                columnsList.add(columnName);
                tableKeyInfo.setColumns(columnsList);
                tableKeyInfo.setIndexComment(rawKeyInfo.get("Index_comment").toString());
                tableKeyInfo.setIndexType(rawKeyInfo.get("Index_type").toString());
                tableKeyInfo.setName(rawKeyInfo.get("Key_name").toString());
                tableKeyInfo.setUnique("0".equals(rawKeyInfo.get("Non_unique").toString()));
            } else {
                tableKeyInfo.getColumns().add(columnName);
            }
            keyMap.put(rawKeyInfo.get("Key_name").toString(), tableKeyInfo);
        }
        List<TableKeyInfo> tableKeyInfoList = new ArrayList<>(keyMap.values());
        return tableKeyInfoList;
    }
}
