package com.github.gianlucafattarsi.liberapi.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "LIBER-API", version = "1.0",
        description = "REST API for LIBER",
        contact = @Contact(name = "Gianluca Fattarsi", email = "gianluca.fattarsi@gmail.com")),
        security = {@SecurityRequirement(name = "bearerAuth")}
)
@SecuritySchemes({
        @SecurityScheme(name = "bearerAuth",
                description = "JWT auth",
                scheme = "bearer",
                type = SecuritySchemeType.HTTP,
                bearerFormat = "JWT",
                in = SecuritySchemeIn.HEADER)
})
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                             .group("all")
                             .pathsToMatch("/api/**")
                             .displayName(" All APIs")
                             .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                             .group("auth")
                             .pathsToMatch("/api/auth/**")
                             .displayName("Authentication APIs")
                             .build();
    }

    @Bean
    public GroupedOpenApi accountsApi() {
        return GroupedOpenApi.builder()
                             .group("accounts")
                             .pathsToMatch("/api/users/**")
                             .displayName("Accounts APIs")
                             .build();
    }

    @Bean
    public GroupedOpenApi libraryApi() {
        return GroupedOpenApi.builder()
                             .group("library")
                             .pathsToMatch("/api/libraries/**")
                             .displayName("Library APIs")
                             .build();
    }
}