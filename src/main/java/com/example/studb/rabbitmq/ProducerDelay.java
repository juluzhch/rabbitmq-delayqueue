package com.example.studb.rabbitmq;

import java.util.Date;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Producer
 *
 * @author zhangchao01
 * @date 2021/6/17
 */
@Component
public class ProducerDelay {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public static final String DELAYED_QUEUE_NAME = "delay.queue.demo.delay.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delay.queue.demo.delay.exchange";
    public static final String DELAYED_ROUTING_KEY = "delay.queue.demo.delay.routingkey";

    public void produce() {
        String message = new Date() + "Beijing";
//        System.out.println("生产者生产消息=====" + message);
//        rabbitTemplate.convertAndSend("rabbitmq_queue",  message);
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("c2", "q2", message);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            System.out.println("生产者生产消息=====" + message);
        }


    }

    public void produce2() {
        for (int i = 0; i < 2; i++) {
            String message = "msg_" + System.currentTimeMillis();
            en e1 = new en();
            e1.setKey(message);
            e1.setName(i + "");
            sendDelayMsg(e1, 2000);
            System.out.println("生产者生产消息=====" + e1);
        }
    }

    public void sendDelayMsg(en msg, Integer delayTime) {
        rabbitTemplate.convertAndSend("delay.queue.demo.business2.exchange", "delay.queue.demo.business.queuec.routingkey", msg, a -> {
            a.getMessageProperties().setExpiration(delayTime.toString());
//            a.getMessageProperties().setDelay(delayTime);
            return a;
        });
    }

    public void produceD() {
        for (int i = 100; i < 103; i++) {
            String message = "msg_" + System.currentTimeMillis();
            en e1 = new en();
            e1.setKey(message);
            e1.setName(i + "");
            sendDelayMsgD(e1, 2000);
            System.out.println("生产者生产消息=====" + e1);
//            try {
//                Thread.sleep(500);
//            }catch (Exception e){
//
//            }
        }
    }

    public void sendDelayMsgD(en msg, Integer delayTime) {
        rabbitTemplate.convertAndSend("delay.queue.demo.business2.exchange", "delay.queue.demo.business.queue_d", msg, a -> {
            a.getMessageProperties().setExpiration(delayTime.toString());
//            a.getMessageProperties().setDelay(delayTime);
            return a;
        });
    }
}
