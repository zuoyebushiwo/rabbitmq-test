package com.zuoye.rabbitmq.ack_queue;

import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;
import com.zuoye.rabbitmq.RabbitMQUtils;

import java.util.Scanner;

/**
 * @author ZhangXueJun
 * @Date 2022年10月18日
 */
public class ConsoleProducer {

    public static final String ACK_QUEUE = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.createChannel();
        channel.queueDeclare(ACK_QUEUE, true, false, false, Maps.newHashMap());

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            channel.basicPublish("",ACK_QUEUE, null, next.getBytes());
        }
    }
}
