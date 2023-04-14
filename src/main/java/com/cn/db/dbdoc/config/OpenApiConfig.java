package com.cn.db.dbdoc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author linht
 */
@Configuration
public class OpenApiConfig {
    /**
     * SpringDoc 标题、描述、版本等信息配置
     *
     * @return openApi 配置信息
     */
    @Bean
    public OpenAPI springDocOpenAPI() {
        return new OpenAPI().info(new Info()
                        .title("数据字典")
                        .description("数据字典接口文档说明")
                        .version("v0.0.1-SNAPSHOT")
                        .license(new License().name("纷飞梦雪")
                                .url("https://blog.csdn.net/Z_linht?type=blog")))
                .externalDocs(new ExternalDocumentation()
                        .description("码云项目地址")
                        .url("https://gitee.com/giteeLinht/db-doc"))
                // 配置Authorizations
                .components(new Components().addSecuritySchemes("bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")));
    }

    /**
     * demo 分组
     *
     * @return demo分组接口
     */
    @Bean
    public GroupedOpenApi siteApi() {
        return GroupedOpenApi.builder()
                .group("默认").packagesToScan("com.cn.db")
                .build();
    }
}