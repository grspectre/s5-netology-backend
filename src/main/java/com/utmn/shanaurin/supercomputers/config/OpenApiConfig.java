package com.utmn.shanaurin.supercomputers.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Supercomputers API")
                        .version("1.0.0")
                        .description("REST API для управления данными о суперкомпьютерах TOP500")
                        .contact(new Contact()
                                .name("Shanaurin")
                                .email("shanaurin@utmn.ru")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development server")
                ));
    }
}
