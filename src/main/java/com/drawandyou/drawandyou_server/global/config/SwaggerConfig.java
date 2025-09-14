package com.drawandyou.drawandyou_server.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Draw And You API")
                .description("Draw And You 서비스의 REST API 문서입니다.")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Draw And You Team")
                        .email("contact.drawandyou@gmail.com")
                        .url("https://www.drawandyou.com"));
    }
}