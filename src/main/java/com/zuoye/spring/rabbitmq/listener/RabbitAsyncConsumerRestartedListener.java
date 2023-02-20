package com.zuoye.spring.rabbitmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.AsyncConsumerRestartedEvent;
import org.springframework.amqp.rabbit.listener.AsyncConsumerStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author ZhangXueJun
 * @Date 2023年02月20日
 */
@Slf4j
public class RabbitAsyncConsumerRestartedListener implements ApplicationListener<AsyncConsumerRestartedEvent> {

    @Override
    public void onApplicationEvent(AsyncConsumerRestartedEvent event) {
      log.info("onApplicationEvent:" + event.toString());
    }
}
