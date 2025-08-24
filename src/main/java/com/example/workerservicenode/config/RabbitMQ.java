package com.example.workerservicenode.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQ {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    // When you define a custom RabbitListenerContainerFactory, it overrides the default configuration that RabbitMQ would normally use for message consumption.
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> prefetchRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setPrefetchCount(EnvConfig.PREFETCH_COUNT); // Set prefetch count here
        factory.setConcurrentConsumers(EnvConfig.CONSUMER_THREAD_COUNT); // Allow 5 threads to process messages concurrently
        factory.setMaxConcurrentConsumers(EnvConfig.CONSUMER_THREAD_COUNT_MAX); // Scale up dynamically
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
