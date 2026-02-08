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
public class ProductServiceClientConfig {
//    @Bean(name = "restClientBuilder")
//    @LoadBalanced
//    public RestClient.Builder restClientBuilder(){
//        return RestClient.builder();
//    }
//    @Bean
//    public ProductServiceClient productServiceClientInterface(RestClient.Builder restClientBuilder){
//        RestClient restClient = restClientBuilder
//                .baseUrl("http://PRODUCT-SERVICE")
//                .defaultStatusHandler(HttpStatusCode::is4xxClientError, ((request,response) -> Optional.empty()))
//                .build();
//        RestClientAdapter adapter = RestClientAdapter.create(restClient);
//        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
//        ProductServiceClient productServiceClient = factory.createClient(ProductServiceClient.class);
//        return productServiceClient;
//    }


    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public ProductServiceClient productServiceClientInterface(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder lbBuilder) {
        RestClient restClient = lbBuilder
                .baseUrl("http://PRODUCT-SERVICE")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, ((request,response) -> Optional.empty()))
                .build();

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(ProductServiceClient.class);
    }
//@Bean
//@LoadBalanced
//public WebClient.Builder webClientBuilder() {
//    return WebClient.builder();
//}
//
//    @Bean
//    public ProductServiceClient productServiceClient(WebClient.Builder webClientBuilder) {
//        WebClient client = webClientBuilder
//                .baseUrl("http://PRODUCT-SERVICE") // Eureka service name
//                .build();
//
//        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client))
//                .build()
//                .createClient(ProductServiceClient.class);
//    }
}
