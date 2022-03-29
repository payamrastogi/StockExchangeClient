package com.coddicted.stockexchangeclient.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumerActionRequest {
    @Builder.Default
    private long timestamp = System.currentTimeMillis();
    private String consumerId;
    private CustomKafkaListenerProperty consumerProperty;
    private Boolean startImmediately;
    private ConsumerAction consumerAction;
}