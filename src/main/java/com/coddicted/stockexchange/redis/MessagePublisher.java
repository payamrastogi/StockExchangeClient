package com.coddicted.stockexchange.redis;

import com.coddicted.stockexchange.model.Message;

public interface MessagePublisher {
    void publish(final Message message);
}