package com.ecomerece.order.clients;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class UserServiceClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilderUser() {
        return RestClient.builder();
    }
    @Bean
    public UserServiceClient userServiceClientInterface(@Qualifier("loadBalancedRestClientBuilderUser") RestClient.Builder lbBuilder1) {
        RestClient restClient = lbBuilder1
                .baseUrl("http://USER-SERVICE")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, ((request, response) -> Optional.empty()))
                .build();

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(UserServiceClient.class);
    }
}
