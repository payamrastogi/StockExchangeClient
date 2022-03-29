package com.coddicted.stockexchangeclient.listener;

import com.coddicted.stockexchangeclient.model.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
@Component
public class StockMessageListener extends CustomMessageListener {
    @Override
    @SneakyThrows
    public KafkaListenerEndpoint createKafkaListenerEndpoint(String name, String topic) {
        MethodKafkaListenerEndpoint<String, String> kafkaListenerEndpoint =
                createDefaultMethodKafkaListenerEndpoint(name, topic);
        kafkaListenerEndpoint.setBean(new StockMessageListener.MyStockMessageListener());
        kafkaListenerEndpoint.setMethod(StockMessageListener.MyStockMessageListener.class.getMethod(
                "onMessage", ConsumerRecord.class));
        return kafkaListenerEndpoint;
    }

    @Slf4j
    private static class MyStockMessageListener implements MessageListener<String, Message> {
        @Override
        public void onMessage(ConsumerRecord<String, Message> record) {
            log.info("My message listener got a new record: " + record);

            CompletableFuture.runAsync(this::sleep)
                    .join();
            log.info("My message listener done processing record: " + record);
        }
        @SneakyThrows
        private void sleep() {
            Thread.sleep(5000);
        }
    }
}
