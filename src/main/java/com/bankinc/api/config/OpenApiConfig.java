package com.bankinc.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Product Management",
                version = "1.0.0",
                description = "Aplicacion para el manejo de productos financieros"

        )
)
public class OpenApiConfig {
}
