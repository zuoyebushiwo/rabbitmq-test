package com.zuoye.rabbitmq.hello_world;

import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;
import com.zuoye.rabbitmq.RabbitMQUtils;

import java.util.concurrent.TimeUnit;

/**
 * RabbitMQ 第一个实例
 *
 * @author ZhangXueJun
 * @Date 2022年10月16日
 */
public class Producer {

    private static final String QUEUE_NAME = "zuoye-hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, Maps.newHashMap());

        // 发送消息
        String message = "hello world";


        int i = 0;
        while (true) {
            try {
                String sed = message + i;
                System.out.println("发送消息成功：" + sed);
                channel.basicPublish("", QUEUE_NAME, null, sed.getBytes());
                i++;
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
