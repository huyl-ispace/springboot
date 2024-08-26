package vn.huyl.service.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "API Documentation", version = "v1"),
    servers = {@Server(url = "${server.servlet.context-path}")},
    security = @SecurityRequirement(name = "keycloak")
)
@SecurityScheme(name = "keycloak", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(
    password = @OAuthFlow(
        tokenUrl = "${keycloak.token.url}"
    )
))
public class SwaggerConfig {
}
