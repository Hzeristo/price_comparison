package com.haydenshui.pricecomparison.item;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "item.queue";
    public static final String EXCHANGE_NAME = "item.exchange";
    public static final String ROUTING_KEY_SPIDER2ITEM = "item.s2i";
    public static final String ROUTING_KEY_ITEM2MERGE = "item.i2m";
    public static final String ROUTING_KEY_SPIDER2MERGE = "item.s2m";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding1(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_SPIDER2ITEM);
    }

    @Bean
    public Binding binding2(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_ITEM2MERGE);
    }

    @Bean
    public Binding binding3(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_SPIDER2MERGE);
    }

    
}
