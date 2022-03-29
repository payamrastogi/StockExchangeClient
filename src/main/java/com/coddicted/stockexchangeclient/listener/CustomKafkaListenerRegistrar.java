package com.coddicted.stockexchangeclient.listener;

import com.coddicted.stockexchangeclient.model.CustomKafkaListenerProperty;
import com.coddicted.stockexchangeclient.configuration.CustomKafkaListenerProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomKafkaListenerRegistrar implements InitializingBean {
    private BeanFactory beanFactory;
    private CustomKafkaListenerProperties customKafkaListenerProperties;
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private KafkaListenerContainerFactory kafkaListenerContainerFactory;

    @Override
    public void afterPropertiesSet() {
        customKafkaListenerProperties.getListeners()
                .forEach(this::registerCustomKafkaListener);
    }
    public void registerCustomKafkaListener(String name, CustomKafkaListenerProperty customKafkaListenerProperty) {
        this.registerCustomKafkaListener(name, customKafkaListenerProperty, false);
    }
    @SneakyThrows
    public void registerCustomKafkaListener(String name,
                                            CustomKafkaListenerProperty customKafkaListenerProperty,
                                            boolean startImmediately) {
        String listenerClass = String.join(".", CustomKafkaListenerRegistrar.class.getPackageName(),
                customKafkaListenerProperty.getListenerClass());
        CustomMessageListener customMessageListener =
                (CustomMessageListener) beanFactory.getBean(Class.forName(listenerClass));
        kafkaListenerEndpointRegistry.registerListenerContainer(
                customMessageListener.createKafkaListenerEndpoint(name, customKafkaListenerProperty.getTopic()),
                kafkaListenerContainerFactory, startImmediately);
    }
}