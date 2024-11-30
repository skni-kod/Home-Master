package com.homestat.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(contact = @Contact(
        name="${OPENAPI_CONTACT_NAME}",
        url = "${OPENAPI_CONTACT_URL}"
        ),
        description = "OpenApi documentation for Wave",
        title = "OpenApi specification",
        version = "1.0",
        license = @License(
                name = "License name",
                url =""
        ),
        termsOfService = "Terms of service"
    ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "${OPENAPI_LOCAL_ENV}"
                )
        },
        security = {
//            @SecurityRequirement(name = "bearerAuth")
        }
)

//TODO: JWT auth
//@Configuration
//@SecurityScheme(
//        name = "bearerAuth",
//        description = "JWT auth description",
//        scheme = "bearer",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        in = SecuritySchemeIn.HEADER
//)
public class OpenApiConfig {}