package com.example.studb.rabbitmq;//package com.example.studb.rabbitmq;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Comsumer
 *
 * @author zhangchao01
 * @date 2021/6/17
 */
@Component
@Slf4j
public class ComsumerDelay {

    public static final String DELAYED_QUEUE_NAME = "delay.queue.demo.delay.queue";


    @RabbitListener(queues = "delay.queue.demo.deadletter.queuec", ackMode = "MANUAL")
    public void process2(Message message, Channel channel) {
        System.out.println("消费者消费消息 -deadLetterQueueC =====" + message + "|" + new String(message.getBody()));
        ackHandler(message, channel);
    }

    @RabbitListener(queues = "delay.queue.demo.deadletter.queue_d")
    public void processD(Message message, Channel channel) {
        System.out.println("消费者消费消息 -deadLetter_Queue_D =====" + new String(message.getBody()));
        ackHandler(message, channel);
    }

    private void ackHandler(Message message, Channel channel) {
        MessageProperties properties = message.getMessageProperties();
        try {
            channel.basicAck(properties.getDeliveryTag(), false);
        } catch (IOException e) {
            System.out.println("channel basicAck error, deliveryTag =" + properties.getDeliveryTag());
        }
    }

}
