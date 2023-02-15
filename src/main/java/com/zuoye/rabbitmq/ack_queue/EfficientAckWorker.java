package com.zuoye.rabbitmq.ack_queue;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.zuoye.rabbitmq.SleepUtils;

import java.util.concurrent.TimeUnit;

import static com.zuoye.rabbitmq.RabbitMQUtils.createChannel;

/**
 * @author ZuoYe
 * @Date 2022年10月18日
 */
public class EfficientAckWorker {

    public static final String ACK_QUEUE = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = createChannel();
        channel.basicQos(1);


        DeliverCallback deliverCallback = (consumerTag, message) -> {
            SleepUtils.sleep(1, TimeUnit.SECONDS);
            System.out.println(new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = (String consumerTag) -> {
            System.out.println("消费消息被取消");
        };

        /**
         * 消费消息
         */
        String s = channel.basicConsume(ACK_QUEUE, false, deliverCallback, cancelCallback);
        System.out.println("积极执行完毕");
    }
}
