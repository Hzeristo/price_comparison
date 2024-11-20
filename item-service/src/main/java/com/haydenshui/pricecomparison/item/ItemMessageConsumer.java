package com.haydenshui.pricecomparison.item;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;

@Service
public class ItemMessageConsumer {

    @Autowired
    private ItemService itemService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleMessage(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String body = new String(message.getBody());
        handleS2IMessage(body);
    }

    private void handleS2IMessage(String body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Item> items = objectMapper.readValue(body, new TypeReference<List<Item>>(){});
            itemService.saveItems(items);
        } catch (Exception e) {
            throw e;
        }
    }
}
