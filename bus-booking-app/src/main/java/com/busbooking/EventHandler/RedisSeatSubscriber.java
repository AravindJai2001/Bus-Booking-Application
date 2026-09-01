package com.busbooking.EventHandler;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSeatSubscriber implements MessageListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        String body = new String(message.getBody());

        messagingTemplate.convertAndSend("/topic/seats", body);
    }
}
