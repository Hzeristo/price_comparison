package com.haydenshui.pricecomparison.item;

import com.haydenshui.pricecomparison.shared.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public class ItemMessageProvider {
    
    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper; // 用于将对象序列化为 JSON

    @Autowired
    public void ItemMessageProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendItems(List<Item> items) {
        try {
            String message = objectMapper.writeValueAsString(items);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_ITEM2MERGE, message);
            System.out.println("Items sent to RabbitMQ: " + message);
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing item list: " + e.getMessage());
        }
    }
    
}
