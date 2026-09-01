package com.busbooking.EventHandler;

import com.busbooking.dto.SeatUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSeatPublisher {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CHANNEL = "seat-updates";

    public void publish(SeatUpdateEvent event){
        redisTemplate.convertAndSend(CHANNEL,  event);
    }

}
