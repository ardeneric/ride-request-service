package com.challenge.ride.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "rideExchange";
    public static final String QUEUE_NAME = "rideRequests";
    public static final String ROUTING_KEY = "rideRequestRoutingKey";

    @Bean
    public Queue rideRequestQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Exchange rideExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue rideRequestQueue, Exchange rideExchange) {
        return BindingBuilder.bind(rideRequestQueue)
                .to(rideExchange)
                .with(ROUTING_KEY)
                .noargs();
    }

    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }
}
