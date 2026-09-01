package com.busbooking.config;

import com.busbooking.EventHandler.RedisSeatSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisSubscriberConfig {

    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory factory,
            RedisSeatSubscriber subscriber
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(factory);
        container.addMessageListener(subscriber, new PatternTopic("seat-updates"));

        return container;
    }
}
