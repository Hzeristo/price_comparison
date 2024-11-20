package com.haydenshui.pricecomparison.item;

import com.haydenshui.pricecomparison.shared.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemMessageConsumer {

    @Autowired
    private ItemService itemService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleMessage(Message message) throws JsonProcessingException {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String body = new String(message.getBody());
        handleS2IMessage(body);
    }

    private void handleS2IMessage(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Item> items = objectMapper.readValue(body, new TypeReference<List<Item>>(){});
        itemService.saveItems(items);
    }
}
