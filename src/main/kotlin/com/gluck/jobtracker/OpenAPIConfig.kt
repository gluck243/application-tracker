package com.gluck.jobtracker

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Job Application Tracker API")
                    .version("1.0")
                    .description("Here are listed Job Application Tracker project's REST API endpoints")
            )

            .components(
                Components()
                .addSecuritySchemes("basicAuth", SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("basic")
                )
            )
            // 2. Apply it globally to all API routes
            .addSecurityItem(SecurityRequirement().addList("basicAuth"))
    }

}