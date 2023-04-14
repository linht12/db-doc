package com.cn.db.dbdoc.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author linht
 */
@Data
@TableName("tb_db")
public class DbEntity {
    @TableField
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    @NotBlank(message="数据库用户名不能为空")
    private String username;
    @NotBlank(message="数据库密码不能为空")
    private String password;
    @NotBlank(message="数据库的url不能为空")
    private String url;
    private String type;
}
