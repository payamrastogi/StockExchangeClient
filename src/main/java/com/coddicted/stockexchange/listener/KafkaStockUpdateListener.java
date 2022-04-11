package com.coddicted.stockexchange.listener;

import com.coddicted.stockexchange.model.Message;
import com.coddicted.stockexchange.redis.RedisMessagePublisher;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaStockUpdateListener {

    private RedisMessagePublisher redisMessagePublisher;

    @KafkaListener(topics = "stockUpdates",
            containerFactory = "stockMessageKafkaListenerContainerFactory")
    public void listenStockUpdates(Message message) {
        System.out.println("Received Message in group foo: " + message.toString());
        redisMessagePublisher.publish(message);
    }
}
