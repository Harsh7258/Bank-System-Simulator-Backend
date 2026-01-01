package org.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("accounts-service", r -> r
                        .path("/api/v2/accounts/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://localhost:8081/"))
                .route("transaction-service", r -> r
                        .path("/api/v2/transactions/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://localhost:8082/"))
                .route("notifications-service", r -> r
                        .path("/api/v2/notifications/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://localhost:8083/"))
                .build();
    }
}
