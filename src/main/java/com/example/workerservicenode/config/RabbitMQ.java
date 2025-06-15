package com.example.workerservicenode.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import util.Env;

@Configuration
public class RabbitMQ {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQ.class);
    private static final int PREFETCH_COUNT = Env.getEnvOrDefault("PREFETCH_COUNT", Integer::parseInt, 10);
    private static final int CONSUMER_THREAD_COUNT = Env.getEnvOrDefault("CONSUMER_THREAD_COUNT", Integer::parseInt, 5);
    private static final int CONSUMER_THREAD_COUNT_MAX = Env.getEnvOrDefault("CONSUMER_THREAD_COUNT_MAX", Integer::parseInt, 10);

    public static int STEP_SIZE_FOR_EXTRACTION = Env.getEnvOrDefault("STEP_SIZE_FOR_EXTRACTION", Integer::parseInt, 5); //PROBABLY ALLOW THE INCOMING REQUEST TO OVERRIDE THIS FOR SAID REQUEST.
    public static int USE_X_FOR_LINES = Env.getEnvOrDefault("USE_X_FOR_LINES", Integer::parseInt, 0); //Defaults to off.

    static{
        logger.info("Initialising Configuration...");
        logger.info("PREFETCH_COUNT: " + PREFETCH_COUNT);
        logger.info("CONSUMER_THREAD_COUNT: " + CONSUMER_THREAD_COUNT);
        logger.info("CONSUMER_THREAD_COUNT_MAX: " + CONSUMER_THREAD_COUNT_MAX);
        logger.info("STEP_SIZE_FOR_EXTRACTION: " + STEP_SIZE_FOR_EXTRACTION);
        logger.info("USE_X_FOR_LINES: " + USE_X_FOR_LINES);
    }

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
        factory.setPrefetchCount(PREFETCH_COUNT); // Set prefetch count here
        factory.setConcurrentConsumers(CONSUMER_THREAD_COUNT); // Allow 5 threads to process messages concurrently
        factory.setMaxConcurrentConsumers(CONSUMER_THREAD_COUNT_MAX); // Scale up dynamically
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
