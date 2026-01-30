package com.gluck.jobtracker;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(
                new Info()
                    .title("Job Application Tracker API")
                    .version("1.0")
                    .description("Here are listed Job Application Tracker project's REST API endpoints")
                )
                .servers(List.of(
                    new Server().url("https://job-application-tracker.up.railway.app/"),
                    new Server().url("http://localhost:8080")
                ))
                .components(
                    new Components()
                        .addSecuritySchemes("basicAuth",
                            new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                        )
                );
        // .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }
}

