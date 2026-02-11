package com.ecomerence.gateway;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomRouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("product-service",  r -> r
                        .path("/api/v1/products/**")
//                        .filters(f -> f.rewritePath("/products(?<segment>/?.*)", "/api/v1/products${segment}"))
                        .uri("http://localhost:8082"))
                .route("user-service", r -> r
                        .path("/api/v1/users/**")
//                        .filters(f -> f.rewritePath("/users(?<segment>/?.*)", "/api/v1/users${segment}"))
                        .uri("http://localhost:8081"))
                .route("order-service", r -> r
                        .path("/api/v1/order/**", "/api/v1/cart/**")
//                        .filters(f -> f.rewritePath("/(?<segment>/?.*)", "/api/v1${segment}"))
                        .uri("http://localhost:8083"))
                .route("eureka-serice", r -> r
                        .path("/eureka/main")
//                        .filters(f -> f.rewritePath("/eureka/main", "/"))
                        .uri("http://localhost:8761"))
                .route("eureka-service-static", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }
}
