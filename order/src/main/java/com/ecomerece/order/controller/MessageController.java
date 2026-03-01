package com.ecomerece.order.controller;


import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {


    @GetMapping("/message")
    @RateLimiter(name = "rateLimiterBreaker", fallbackMethod = "getMessageFallBack")
    public String getMessages(){
        return "Hello Order";


    }
    public String getMessageFallBack(Exception e){
        return "Hello Fallback";
    }

}
