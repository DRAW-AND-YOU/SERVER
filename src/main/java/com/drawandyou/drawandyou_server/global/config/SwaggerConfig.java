package com.drawandyou.drawandyou_server.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private final Environment environment;

    public SwaggerConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenAPI openAPI() {
        // 현재 실행 환경에 맞는 서버 URL 설정
        String serverUrl = getServerUrl();

        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(new Server().url(serverUrl).description("API Server")));
    }

    private String getServerUrl() {
        // 프로덕션 환경에서는 HTTPS 사용
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("prod".equals(profile)) {
                return "https://api.drawandyou.com";
            }
            if ("dev".equals(profile)) {
                return "https://api.drawandyou.com";
            }
        }
        // 로컬 환경
        return "http://localhost:8080";
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