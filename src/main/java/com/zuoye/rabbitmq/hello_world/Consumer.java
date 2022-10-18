package com.zuoye.rabbitmq.hello_world;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.zuoye.rabbitmq.RabbitMQUtils.createChannel;

/**
 * @author ZhangXueJun
 * @Date 2022年10月16日
 */
public class Consumer {

    public static final String QUEUE_NAME = "zuoye-hello";

    public static void main(String[] args) throws Exception {
        Channel channel = createChannel();


        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
//                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = (String consumerTag) -> {
            System.out.println("消费消息被取消");
        };

        /**
         * 消费消息
         */
        String s = channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        System.out.println("执行完毕");

    }
}
