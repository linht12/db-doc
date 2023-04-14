package com.cn.db.dbdoc.enums;

/**
 * @author linht
 */
public enum DbType {
    MYSQL("MYSQL","com.mysql.cj.jdbc.Driver"),
    ORACLE("oracle","oracle.jdbc.driver.OracleDriver"),
    POSTGRESQL("postgresql","org.postgresql.Driver");
    private String type;

    private String aClassString;

    private DbType(String type, String aClass) {
        this.type = type;
        this.aClassString = aClass;
    }
    public static String findClass(String type){
        for (DbType value : DbType.values()) {
            if (value.type.equalsIgnoreCase(type))
            {
                return value.aClassString;
            }
        }
        throw  new RuntimeException("找不到匹配的数据库类型");
    }

}
