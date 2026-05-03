package com.brunoandradesa.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Spring Boot API - Base Project")
                .version("v1")
                .description("Projeto base para criação de APIs com o framework Spring Boot")
                .license(new License().name("MIT").url("https://mit-license.org/")));
  }
}
