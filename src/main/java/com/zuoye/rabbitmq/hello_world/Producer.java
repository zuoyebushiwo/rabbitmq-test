package com.zuoye.rabbitmq.hello_world;

import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ 第一个实例
 *
 * @author ZhangXueJun
 * @Date 2022年10月16日
 */
public class Producer {

    private static final String QUEUE_NAME = "zuoye-hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(12345);
        factory.setUsername("guest");
        factory.setPassword("guest");

        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, Maps.newHashMap());

            // 发送消息
            String message = "hello world";

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完毕");


        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
