package com.zuoye.spring.rabbitmq;

import com.rabbitmq.client.Channel;
import com.zuoye.binlog.mysql.MyConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.concurrent.TimeUnit;

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
    public ApplicationRunner runner1(RabbitTemplate template) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
//                for (int i = 0; i < 10; i++) {
//                    CorrelationData cd1 = new CorrelationData();
//                    cd1.setId(i+" ");
//                    template.convertAndSend("", "myqueue",  "foo"+ i, cd1);
//                    TimeUnit.SECONDS.sleep(5);
//
//                    log.info("CorrelationData:{} {} {}", cd1.getId());
//                }
            }
        };
    }

    @Bean
    public ApplicationRunner runner2(RabbitTemplate template) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                for (int i = 0; i < 10; i++) {
                    CorrelationData cd1 = new CorrelationData();
                    cd1.setId(i+" ");
//                    Message build = MessageBuilder
//                            .withBody(("foo" + i).getBytes(StandardCharsets.UTF_8))
//                            .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
//                            .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
//                            .setMessageId("foo" + i)
//                            .setHeader("a", "b")
//                            .build();

                    MyConf column = new MyConf();
                    column.setPort(3);

                    template.setRoutingKey("myqueue");
                    template.convertAndSend(column);
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
    public void listen(@Payload MyConf in, Channel channel) {
        log.info("RabbitListener:"+ in.toString());

    }
}
