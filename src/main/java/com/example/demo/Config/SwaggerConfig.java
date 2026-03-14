package com.example.demo.Config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// ✅ Swagger UI available at: http://localhost:8082/swagger-ui/index.html
// ✅ OpenAPI JSON at:         http://localhost:8082/v3/api-docs
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI jobPortalOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Job Portal API")
                .description("REST API for Job Portal — Authentication, Jobs, Applications, Resume")
                .version("v1.0")
                .contact(new Contact()
                    .name("Anchal Yadav")
                    .email("aymyav007@gmail.com")))
            // ✅ Adds "Authorize" button in Swagger UI to test protected endpoints
            .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
            .components(new Components()
                .addSecuritySchemes("BearerAuth", new SecurityScheme()
                    .name("BearerAuth")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
}