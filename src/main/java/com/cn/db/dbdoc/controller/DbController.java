package com.cn.db.dbdoc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cn.db.dbdoc.entity.DbEntity;
import com.cn.db.dbdoc.entity.TableInfo;
import com.cn.db.dbdoc.mapper.DbMapper;
import com.cn.db.dbdoc.servjce.impl.MySqlDbInfoServiceImpl;
import com.cn.db.dbdoc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author linht
 */
@RestController
@Slf4j
@RequestMapping("/db")
public class DbController{

    @Autowired
    private DbMapper dbMapper;
    @Autowired
    private MySqlDbInfoServiceImpl dbInfoService;

    @PostMapping("/add")
    public DbEntity addDB(@Validated @RequestBody DbEntity entity){
        entity.setId(IdWorker.getId());
        if(entity.getType().isBlank()){
            entity.setType("MYSQL");
        }
        DbUtils.test(entity);
        dbMapper.insert(entity);
        entity=dbMapper.selectById(entity.getId());
        return entity;
    }
    @GetMapping("/list")
    public List<DbEntity> list(){
        List<DbEntity> dbEntities = dbMapper.selectList(new QueryWrapper<>());
        return dbEntities;
    }

    @GetMapping("/test")
    public List<TableInfo> test(String id){
        DbEntity dbEntity = dbMapper.selectById(id);
        List<TableInfo> tableInfos = dbInfoService.tableInfoList(dbEntity);
        return tableInfos;
    }
}
