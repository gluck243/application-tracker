package com.gluck.jobtracker

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
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
    }

}