package com.zuoye.spring.rabbitmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.ListenerContainerConsumerFailedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author ZhangXueJun
 * @Date 2023年02月20日
 */
@Slf4j
public class RabbitContainerConsumerFailedListener implements ApplicationListener<ListenerContainerConsumerFailedEvent> {

    @Override
    public void onApplicationEvent(ListenerContainerConsumerFailedEvent event) {
      log.info("onApplicationEvent:" + event.toString());
    }
}
