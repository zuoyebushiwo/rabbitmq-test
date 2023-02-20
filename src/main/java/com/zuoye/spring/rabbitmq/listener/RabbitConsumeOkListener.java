package com.zuoye.spring.rabbitmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.AsyncConsumerRestartedEvent;
import org.springframework.amqp.rabbit.listener.ConsumeOkEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author ZhangXueJun
 * @Date 2023年02月20日
 */
@Slf4j
public class RabbitConsumeOkListener implements ApplicationListener<ConsumeOkEvent> {

    @Override
    public void onApplicationEvent(ConsumeOkEvent event) {
      log.info("onApplicationEvent:" + event.toString());
    }
}
