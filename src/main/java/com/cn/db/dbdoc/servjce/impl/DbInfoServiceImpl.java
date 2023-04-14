package com.cn.db.dbdoc.servjce.impl;

import com.cn.db.dbdoc.entity.DbEntity;
import com.cn.db.dbdoc.entity.TableInfo;
import com.cn.db.dbdoc.mapper.DbMapper;
import com.cn.db.dbdoc.servjce.DbInfoService;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * @author linht
 */
public abstract class DbInfoServiceImpl implements DbInfoService {
    @Resource
    private DbMapper dbMapper;

    @Override
    public String databaseName(Long dbId) {

        return this.databaseName(this.findDbEntity(dbId));
    }

    @Override
    public List<TableInfo> tableInfoList(Long dbId) {
        return this.tableInfoList(this.findDbEntity(dbId));
    }

    private DbEntity findDbEntity(Long dbId) {
        DbEntity dbEntity = dbMapper.selectById(dbId);
        if (dbEntity == null || dbEntity.getId() == null) {
            throw new RuntimeException("查询数据无效");
        }
        return dbEntity;
    }
}
