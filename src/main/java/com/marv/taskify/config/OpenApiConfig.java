package com.marv.taskify.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskifyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Taskify API")
                        .description("A simple personal task manager backend built with Spring Boot & JWT")
                        .version("v1.0.0"));
    }

//    @Bean
//    public GroupedOpenApi authApi() {
//        return GroupedOpenApi.builder()
//                .group("auth")
//                .pathsToMatch("/api/v1/auth/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi taskApi() {
//        return GroupedOpenApi.builder()
//                .group("tasks")
//                .pathsToMatch("/api/v1/tasks/**")
//                .build();
//    }

}
