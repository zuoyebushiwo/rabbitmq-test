package com.zuoye.spring.rabbitmq.message;

import com.rabbitmq.client.MessageProperties;
import lombok.Getter;

/**
 * @author ZuoYe
 * @Date 2023年02月13日
 */
public class Message {

    @Getter
    private final MessageProperties messageProperties;

    @Getter
    private final byte[] body;

    public Message(MessageProperties messageProperties, byte[] body) {
        this.messageProperties = messageProperties;
        this.body = body;
    }
}
