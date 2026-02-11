package com.ecomerece.order.clients;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Autowired(required = false)
    private ObservationRegistry observationRegistry;
    @Autowired(required = false)
    private Tracer tracer;
    @Autowired(required = false)
    private Propagator propagator;
    @Bean
    @Primary
    public RestClient.Builder restClientBuilder() {
         RestClient.Builder builder = RestClient.builder();
         if (observationRegistry != null) {
             builder.requestInterceptor(creatingTraceInterceptor());
         }
         return builder;
    }

    public ClientHttpRequestInterceptor creatingTraceInterceptor() {
        return ((request,body,execution) -> {
            return Observation.createNotStarted("http.client.request",observationRegistry)
                    .observe(() -> {
                        if(tracer != null && propagator != null && tracer.currentSpan() != null) {
                            propagator
                                    .inject(tracer.currentTraceContext().context()
                                            , request.getHeaders()
                                            , (carrier,key, value) -> carrier
                                                    .add(key, value));
                        }

                        try{
                            return execution.execute(request,body);
                        }catch (Exception e){
                            throw new RuntimeException(e);
                        }
                    });


        });
    }
}
