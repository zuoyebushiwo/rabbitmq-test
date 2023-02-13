package com.zuoye.spring.rabbitmq;

import com.zuoye.spring.rabbitmq.config.RabbitConfiguration;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author ZhangXueJun
 * @Date 2023年02月13日
 */
@SpringBootApplication
public class SpringRabbitApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringRabbitApplication.class, args);

        ApplicationContext context =
                new AnnotationConfigApplicationContext(RabbitConfiguration.class);
        AmqpTemplate template = context.getBean(AmqpTemplate.class);
        template.convertAndSend("myqueue", "foo");
        String foo = (String) template.receiveAndConvert("myqueue");
        System.out.println(foo);
    }
}
