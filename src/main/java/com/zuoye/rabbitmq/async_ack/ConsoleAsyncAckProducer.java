package com.zuoye.rabbitmq.async_ack;

import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.zuoye.rabbitmq.RabbitMQUtils;

import java.util.Scanner;

/**
 * @author ZuoYe
 * @Date 2022年10月18日
 */
public class ConsoleAsyncAckProducer {

    public static final String ACK_QUEUE = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.createChannel();


        channel.confirmSelect();
        ConfirmCallback ackCallback = (long deliveryTag, boolean multiple) -> {
            System.out.println("接收到确认消息：" + deliveryTag);
        };

        ConfirmCallback nackCallback = (long deliveryTag, boolean multiple) -> {
            System.out.println("接收到未确认消息：" + deliveryTag);
        };
        channel.addConfirmListener(ackCallback, nackCallback);

        channel.queueDeclare(ACK_QUEUE, true, false, false, Maps.newHashMap());

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            channel.basicPublish("",ACK_QUEUE, null, next.getBytes());
        }
    }
}
