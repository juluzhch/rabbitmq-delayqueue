package com.example.studb.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

public class JacksonMessageConverter extends Jackson2JsonMessageConverter {

    public JacksonMessageConverter() {
        super();
    }

    @Override
    public Object fromMessage(Message message) {
        message.getMessageProperties().setContentType("\"application/json;charset=UTF-8\"");
        return super.fromMessage(message);
    }
}
