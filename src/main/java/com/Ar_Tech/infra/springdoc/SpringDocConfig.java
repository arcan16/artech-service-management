package com.Ar_Tech.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    OpenAPI customeOpenApi(){
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-key",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JJWT")))
                .info(new Info()
                        .title("Ar Tech Servicio tecnico especializado")
                        .description("API REST diseñada para la administracion del taller de servicio tecnico")
                        .contact(new Contact().name("Support").email("support@artech.com"))
                        .license(new License().name("apache 2.0").url("http://localhost.com")));
    }
}
