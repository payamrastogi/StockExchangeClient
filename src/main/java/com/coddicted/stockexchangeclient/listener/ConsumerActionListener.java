package com.coddicted.stockexchangeclient.listener;

import com.coddicted.stockexchangeclient.model.Constant;
import com.coddicted.stockexchangeclient.model.ConsumerActionRequest;
import com.coddicted.stockexchangeclient.model.CustomKafkaListenerProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumerActionListener {

    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private ObjectMapper objectMapper;
    private CustomKafkaListenerRegistrar customKafkaListenerRegistrar;

    @SneakyThrows
    @KafkaListener(topics = Constant.CONSUMER_ACTION_TOPIC,
            id = "my-message-consumer-#{T(java.util.UUID).randomUUID().toString()}")
    public void consumerActionListener(ConsumerRecord<String, String> record) {
        log.info("Consumer action listener got a new record: " + record);
        ConsumerActionRequest consumerActionRequest
                = objectMapper.readValue(record.value(),
                    ConsumerActionRequest.class);
        processAction(consumerActionRequest);
        log.info("Consumer action listener done processing record: " + record);
    }
    private void processAction(ConsumerActionRequest request) {
        String consumerId = request.getConsumerId();
        MessageListenerContainer listenerContainer = Optional.ofNullable(consumerId)
                .map(kafkaListenerEndpointRegistry::getListenerContainer)
                .orElse(null);
        switch (request.getConsumerAction()) {
            case ACTIVATE:
                log.info("Running a consumer with id " + consumerId);
                listenerContainer.start();
                break;
            case PAUSE:
                log.info("Pausing a consumer with id " + consumerId);
                listenerContainer.pause();
                break;
            case RESUME:
                log.info("Resuming a consumer with id " + consumerId);
                listenerContainer.resume();
                break;
            case DEACTIVATE:
                log.info("Stopping a consumer with id " + consumerId);
                listenerContainer.stop();
                break;
            case CREATE:
                CustomKafkaListenerProperty consumerProperty = request.getConsumerProperty();
                log.info(String.format("Creating a %s consumer for topic %s",
                        consumerProperty.getListenerClass(), consumerProperty.getTopic()));
                customKafkaListenerRegistrar.registerCustomKafkaListener(null,
                        consumerProperty, request.getStartImmediately());
                break;
            default:
                log.warn("Consumer action listener do not know action: " +
                        request.getConsumerAction());
        }
    }
}
