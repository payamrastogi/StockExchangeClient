package com.coddicted.stockexchange.redis;

import com.coddicted.stockexchange.model.Message;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RedisMessagePublisher implements MessagePublisher {

    //@Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void publish(Message message) {
        redisTemplate.convertAndSend(message.getCompanyId(), message);
    }
}