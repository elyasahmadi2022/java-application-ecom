package com.example.notification;


import com.example.notification.payload.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class OrderEventConsumer {

    @Bean
    public Consumer<OrderCreatedEvent> orderCreatedEventConsumer() {
        return event -> {
            log.info("event received orderId: {}", event.getOrderId());
            log.info("event received  userId: {}", event.getUserId());
        };
    }

//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void  handleOrderEvent(OrderCreatedEvent orderEvent){
//        System.out.println("Received Order Event: " + orderEvent);
//    }

}
