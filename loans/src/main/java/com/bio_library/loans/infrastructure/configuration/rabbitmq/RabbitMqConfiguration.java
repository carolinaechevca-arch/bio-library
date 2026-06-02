package com.bio_library.loans.infrastructure.configuration.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer,
                                         org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        configurer.configure(rabbitTemplate, connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setObservationEnabled(true);
        return rabbitTemplate;
    }
}
