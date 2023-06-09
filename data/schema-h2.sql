DROP TABLE IF EXISTS tb_db;

CREATE TABLE tb_db
(
    id BIGINT NOT NULL COMMENT '主键ID',
    username VARCHAR(30) NULL DEFAULT NULL COMMENT '用户名',
    type VARCHAR(10) NOT NULL DEFAULT 'mysql' COMMENT '数据库类型',
    password VARCHAR(200) NULL DEFAULT NULL COMMENT '密码',
    url VARCHAR(150) NULL DEFAULT NULL COMMENT '数据库url',
    PRIMARY KEY (id)
);