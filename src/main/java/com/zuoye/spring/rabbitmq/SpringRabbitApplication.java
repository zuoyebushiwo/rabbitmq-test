package com.zuoye.spring.rabbitmq;

import com.zuoye.spring.rabbitmq.config.RabbitConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author ZuoYe
 * @Date 2023年02月13日
 */
@Slf4j
@SpringBootApplication
public class SpringRabbitApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringRabbitApplication.class, args);

//        ApplicationContext context =
//                new AnnotationConfigApplicationContext(RabbitConfiguration.class);
//        AmqpTemplate template = context.getBean(AmqpTemplate.class);
//        template.convertAndSend("myqueue", "foo");
//        String foo = (String) template.receiveAndConvert("myqueue");
//        System.out.println(foo);
    }

    @Bean
    public ApplicationRunner runner(RabbitTemplate template) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                for (int i = 0; i < 10; i++) {
                    CorrelationData cd1 = new CorrelationData();
                    cd1.setId(i+" ");
                    template.convertAndSend("", "myqueue",  "foo"+ i, cd1);
                    TimeUnit.SECONDS.sleep(5);

                    log.info("CorrelationData:{} {} {}", cd1.getId());
                }
            }
        };
    }

    @Bean
    public Queue queue() {
        return new Queue("myqueue");
    }

    @RabbitListener(queues = "myqueue")
    public void listen(String in) {
        System.out.println(in);
    }
}
