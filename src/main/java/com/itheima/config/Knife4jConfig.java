package com.itheima.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("大事件项目API文档")
                        .description("这是大事件项目的API接口文档，使用Knife4j和OpenAPI 3.0生成")
                        .version("1.0")
                        .contact(new Contact()
                                .name("itheima")
                                .url("http://www.itheima.com")
                                .email("support@itheima.com"))
                );
    }
}