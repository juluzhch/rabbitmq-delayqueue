package com.example.studb.rabbitmq;


import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mq 中exchange ,队列，bind 的声明
 */
@Configuration
public class RabbitMQConfig2 {

    // 声明延时Exchange-这个可以时共用的exchange
    public static final String DELAY_EXCHANGE_NAME = "delay.queue.demo.business2.exchange";
    //延时队列-C
    public static final String DELAY_QUEUEA_NAME = "delay.queue.demo.business.queuec";
    //延时队列-C 的路由key
    public static final String DELAY_QUEUEc_ROUTING_KEY = "delay.queue.demo.business.queuec.routingkey";
    //延时队列C的死信路由key (私信队列C绑定时也要使用这个key)
    public static final String DEAD_LETTER_QUEUEC_ROUTING_KEY = "delay.queue.demo.deadletter.delay_10s.routingkey";

    // 声明死信Exchange--可以多个延迟队列共用的私信exchange
    public static final String DEAD_LETTER_EXCHANGE = "delay.queue.demo.deadletter2.exchange";
    //死信队列c
    public static final String DEAD_LETTER_QUEUEA_NAME = "delay.queue.demo.deadletter.queuec";


    //延迟队列D
    public static final String DELAY_QUEUED_NAME = "delay.queue.demo.business.queue_d";
    //延迟队列D对应的死信队列
    public static final String DEAD_LETTER_QUEUED_NAME = "delay.queue.demo.deadletter.queue_d";


    // 声明延时Exchange
    @Bean("delayExchange")
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE_NAME);
    }

    // 声明死信Exchange
    @Bean("deadLetterExchange")
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // 声明延时队列C 延时10s
    // 并绑定到对应的死信交换机
    @Bean("delayQueueC")
    public Queue delayQueueC() {
        Map<String, Object> args = new HashMap<>(2);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUEC_ROUTING_KEY);
//        // x-message-ttl  声明队列的TTL
//        args.put("x-message-ttl", 6000);
        return QueueBuilder.durable(DELAY_QUEUEA_NAME).withArguments(args).build();
    }


    // 声明延时队列A绑定关系，绑定到延迟交换机
    @Bean
    public Binding delayBindingC(@Qualifier("delayQueueC") Queue queue,
        @Qualifier("delayExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAY_QUEUEc_ROUTING_KEY);
    }


    // 声明死信队列C
    @Bean("deadLetterQueueC")
    public Queue deadLetterQueueC() {
        return new Queue(DEAD_LETTER_QUEUEA_NAME);
    }

    // 声明死信队列C绑定关系
    @Bean
    public Binding deadLetterBindingC(@Qualifier("deadLetterQueueC") Queue queue,
        @Qualifier("deadLetterExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUEC_ROUTING_KEY);
    }

    @Bean("delayQueueD")
    public Queue delayQueueD() {
        Map<String, Object> args = new HashMap<>(2);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUED_NAME);
//        // x-message-ttl  声明队列的TTL
//        args.put("x-message-ttl", 6000);
        return QueueBuilder.durable(DELAY_QUEUED_NAME).withArguments(args).build();
    }

    @Bean
    public Binding delayBindingD(@Qualifier("delayQueueD") Queue queue,
        @Qualifier("delayExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAY_QUEUED_NAME);
    }

    @Bean("deadLetterQueueD")
    public Queue deadLetterQueueD() {
        return new Queue(DEAD_LETTER_QUEUED_NAME);
    }

    // 声明死信队列A绑定关系
    @Bean
    public Binding deadLetterBindingD(@Qualifier("deadLetterQueueD") Queue queue,
        @Qualifier("deadLetterExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUED_NAME);
    }

}
