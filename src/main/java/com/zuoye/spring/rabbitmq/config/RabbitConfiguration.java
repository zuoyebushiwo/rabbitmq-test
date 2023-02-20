package com.zuoye.spring.rabbitmq.config;

import com.google.common.collect.Lists;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownSignalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.CorrelationDataPostProcessor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Properties;

/**
 * @author ZuoYe
 * @Date 2023年02月13日
 */
@Slf4j
@Configuration
public class RabbitConfiguration {

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost", 12345);

        ConnectionListener connectionListener = new ConnectionListener() {
            @Override
            public void onCreate(Connection connection) {
                log.info("rabbit connection onCreate.......");
            }

            @Override
            public void onClose(Connection connection) {
                log.info("rabbit connection onClose.......");
            }

            @Override
            public void onFailed(Exception exception) {
                log.info("rabbit connection onFailed.......");
            }

            @Override
            public void onShutDown(ShutdownSignalException signal) {
                signal.getReason();
                log.info("rabbit connection onShutDown.......");
            }
        };


        ChannelListener channelListener = new ChannelListener() {
            @Override
            public void onCreate(Channel channel, boolean transactional) {
                log.info("rabbit channel onCreate ............" + transactional);
            }

            @Override
            public void onShutDown(ShutdownSignalException signal) {
                log.info("rabbit channel onShutDown ............");
            }
        };


        connectionFactory.getRabbitConnectionFactory().getClientProperties().put("aaaa", "1111111");
        connectionFactory.setChannelListeners(Lists.newArrayList(channelListener));
        connectionFactory.setConnectionListeners(Lists.newArrayList(connectionListener));
        connectionFactory.setConnectionNameStrategy(new ConnectionNameStrategy() {
            @Override
            public String obtainNewConnectionName(ConnectionFactory connectionFactory) {
                return "rabbitmq-connector";
            }
        });

        connectionFactory.setChannelCacheSize(10);

//        Properties cacheProperties = connectionFactory.getCacheProperties();
//        log.info("rabbitmq cache properties: {}", cacheProperties.toString());

        connectionFactory.setPublisherReturns(true);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        return connectionFactory;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }


    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(10.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        rabbitTemplate.setUsePublisherConnection(true);
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                log.info(returned.toString());
            }
        });

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                 log.info((correlationData == null ? "": correlationData.toString()) +ack+ (cause == null ? "" : cause));
            }
        });
        rabbitTemplate.setBeforePublishPostProcessors(new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                log.info("setBeforePublishPostProcessors: " + message.toString());
                return message;
            }
        });
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setCorrelationDataPostProcessor(new CorrelationDataPostProcessor() {
            @Override
            public CorrelationData postProcess(Message message, CorrelationData correlationData) {
                log.info("setBeforePublishPostProcessors: " + message.toString() + " correlationData:" + correlationData);
                return correlationData;
            }
        });

        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    @Bean
    public Queue myQueue() {
        return new Queue("myqueue");
    }
}
