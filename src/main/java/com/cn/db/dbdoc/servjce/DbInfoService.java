package com.cn.db.dbdoc.servjce;

import com.cn.db.dbdoc.entity.DbEntity;
import com.cn.db.dbdoc.entity.TableInfo;

import java.util.List;

/**
 * @author linht
 */
public interface DbInfoService {

    /**
     * 获取
     * @return
     */
    String databaseName(DbEntity dbEntity);

    /**
     * 获取
     * @return
     */
    String databaseName(Long dbId);


    /***
     * 获取表的信息
     *
     * @return
     */
    List<TableInfo> tableInfoList(DbEntity dbEntity);

    /**
     * 根据id查询
     * @param dbId
     * @return
     */
    List<TableInfo> tableInfoList(Long dbId);
}
