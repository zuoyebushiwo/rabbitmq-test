package com.zuoye.spring.rabbitmq.quick;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author ZuoYe
 * @Date 2023年02月13日
 */
public class QuickStarter {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new CachingConnectionFactory("127.0.0.1", 12345);
        AmqpAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(new Queue("myqueue"));
        AmqpTemplate template = new RabbitTemplate(connectionFactory);
        template.convertAndSend("myqueue", "foo");
        String foo = (String) template.receiveAndConvert("myqueue");

        System.out.println(foo);

        ApplicationContext context = new GenericXmlApplicationContext("classpath:/rabbit-context.xml");
        template = context.getBean(AmqpTemplate.class);
        template.convertAndSend("myqueue", "foo");

        foo = (String) template.receiveAndConvert("myqueue");

        System.out.println(foo);
    }
}
