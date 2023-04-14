package com.cn.db.dbdoc.util;


import com.cn.db.dbdoc.entity.DbEntity;
import com.cn.db.dbdoc.enums.DbType;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linht
 */
@Slf4j
public class DbUtils {
    private static final int CONNECTION_TIMEOUTS_SECONDS = 6;
    public static Connection getConnection(DbEntity dbEntity) throws ClassNotFoundException, SQLException {
        DriverManager.setLoginTimeout(CONNECTION_TIMEOUTS_SECONDS);

        Class.forName(DbType.findClass(dbEntity.getType()));

        Connection connection = DriverManager.getConnection(dbEntity.getUrl(), dbEntity.getUsername(), dbEntity.getPassword());
//        if (dbEntity.getUrl() == "") {
//            ((OracleConnection) connection).setRemarksReporting(true);
//        }

        return connection;
    }
    public static void close(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * 测试接口
     * @param entity
     * @return
     */
    public static void test(DbEntity entity){
        try {
            Connection connection = getConnection(entity);
            close(connection);
        } catch (ClassNotFoundException|SQLException e) {
            log.error("",e);
            throw  new RuntimeException("连接失败，检查对应类型和数据库账号密码等");
        }
    }


    /**
     * 执行
     * @param dbEntity
     * @param sql
     * @param columns
     * @return
     */
    public static List<Map<String,Object>> executeQuery(DbEntity dbEntity,String sql,String... columns){
        List<Map<String,Object>> list=new ArrayList<>();
        try (Connection connection = DbUtils.getConnection(dbEntity);
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery();){
            while (resultSet.next()){
                Map<String,Object> map =new HashMap<>();
                for (String column : columns) {
                    map.put(column,resultSet.getObject(column));
                }
                list.add(map);
            }
        } catch (ClassNotFoundException |SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
